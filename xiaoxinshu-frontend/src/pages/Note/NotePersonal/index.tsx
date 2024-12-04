import MdEditor from '@/components/Markdown/MdEditor';
import MdViewer from '@/components/Markdown/MdViewer';
import NoteShareModal from '@/components/NoteShareModal';
import TagList from '@/components/TagList';
import { listCategoriesVoByPage } from '@/services/xiaoxinshu/categoriesController';
import { uploadFile } from '@/services/xiaoxinshu/fileController';
import { editNote, getNotePersonalVoById } from '@/services/xiaoxinshu/noteController';
import { useParams } from '@@/exports';
import { FireOutlined, HeartOutlined, LikeOutlined, UploadOutlined } from '@ant-design/icons';
import {
  ProDescriptions,
  ProFormRadio,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
} from '@ant-design/pro-components';
import { ProForm, ProFormInstance } from '@ant-design/pro-form/lib';
import { Button, Card, Flex, Image, Input, message, Space, Tag, Upload } from 'antd';
import Title from 'antd/es/typography/Title';
import React, { useEffect, useRef, useState } from 'react';

/**
 * 个人笔记详情页
 * @constructor
 */
const NotePersonalPage: React.FC = () => {
  const params = useParams();
  const { noteId } = params;
  const [note, setNote] = useState<API.NotePersonalVO>({});
  // 笔记分类列表
  const [categoriesList, setCategoriesList] = useState<API.CategoriesVO[]>([]);
  const [contentMd, setContentMd] = useState<string>('');
  const [isEdit, setIsEdit] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(false);
  const [shareModalVisible, setShareModalVisible] = useState<boolean>(false);
  const formRef = useRef<ProFormInstance>();
  const [tags, setTags] = useState<string[]>([]);
  const [inputValue, setInputValue] = useState('');
  const [noteAvatar, setNoteAvatar] = useState<string>(note?.picture || '');
  const [showPic, setShowPic] = useState<boolean>(false);
  const [picList, setPicList] = useState<string[]>([]);
  const loadData = async () => {
    setLoading(true);
    if (!noteId) {
      message.error('笔记不存在');
      return;
    }
    try {
      const res = await getNotePersonalVoById({
        id: noteId as any,
      });
      setNote(res.data ?? {});
      setContentMd(res.data?.content || '');
      setTags(res.data?.tagList || []);
      setNoteAvatar(res.data?.picture || '');
    } catch (e: any) {
      message.error('获取笔记详情失败' + e.message);
    }
    try {
      const res = await listCategoriesVoByPage({
        sortField: 'priority',
        sortOrder: 'descend',
      });
      setCategoriesList(res.data?.records ?? []);
    } catch (e: any) {
      message.error('获取笔记分类失败，' + e.message);
    }
    setLoading(false);
  };
  useEffect(() => {
    loadData().then(() => '');
  }, []);
  const categoriesIdList = note.categoriesVOList?.map((value) => value.id);
  const categoriesListForm = categoriesList.map((categories) => ({
    value: categories.id,
    label: categories.name,
  }));
  const reviewColumns = [
    {
      title: '审核状态',
      dataIndex: 'reviewStatus',
      valueEnum: {
        0: {
          text: '待审核',
          status: 'Default',
        },
        1: {
          text: '审核通过',
          status: 'Success',
        },
        2: {
          text: '审核不通过',
          status: 'error',
        },
      },
    },
    {
      title: '审核信息',
      dataIndex: 'reviewMessage',
      valueType: 'text',
    },
    {
      title: '审核人 id',
      dataIndex: 'reviewerId',
      valueType: 'text',
    },
    {
      title: '审核时间',
      dataIndex: 'reviewTime',
      valueType: 'dateTime',
    },
  ];
  const otherColumns = [
    {
      title: '所属分类',
      dataIndex: 'categoriesVOList',
      valueType: 'text',
      render: () => {
        return note.categoriesVOList?.map((categories) => {
          return <Tag key={categories.id}>{categories.name}</Tag>;
        });
      },
    },
    {
      title: '可见范围',
      dataIndex: 'visible',
      valueEnum: {
        0: {
          text: '公开',
        },
        1: {
          text: '仅自己可见',
        },
      },
    },
    {
      title: '是否置顶',
      dataIndex: 'isTop',
      valueEnum: {
        0: {
          text: '未置顶',
        },
        1: {
          text: '置顶',
        },
      },
    },
    {
      title: '编辑时间',
      dataIndex: 'editTime',
      valueType: 'dateTime',
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      valueType: 'dateTime',
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'dateTime',
    },
  ];
  // 回车时添加标签
  const handlePressEnter = () => {
    if (inputValue && !tags.includes(inputValue)) {
      setTags([...tags, inputValue]);
      setInputValue(''); // 清空输入框
    }
  };

  // 删除标签
  const handleClose = (removedTag: any) => {
    setTags(tags.filter((tag) => tag !== removedTag));
  };
  const onSubmitFrom = async (value: API.NoteEditRequest, tags: string[]) => {
    setLoading(true);
    try {
      await editNote({
        id: noteId as any,
        ...value,
        tags: tags,
        picture: noteAvatar,
        isTop: value.isTop ? 1 : 0,
      });
      message.success('题目修改成功，等待管理员审核');
      formRef.current?.resetFields();
      setIsEdit(false);
    } catch (e: any) {
      message.error('笔记更新失败，', e);
    }
    setLoading(false);
    await loadData();
  };
  // 笔记封面图片上传
  const handleAvatarUpload = async (options: any) => {
    const { file } = options;
    // 构造请求参数
    const params: API.uploadFileParams = {
      uploadFileRequest: {
        biz: 'note_avatar',
        bizId: noteId as any,
      },
    };
    // 使用 FormData 包装文件数据
    const formData = new FormData();
    formData.append('file', file);
    try {
      const res = await uploadFile(params, formData);
      if (res.data) {
        setNoteAvatar(res.data);
      }
      message.success('图片上传成功');
    } catch (e: any) {
      message.error('图片上传失败，' + e.message);
    }
  };

  // 笔记内容图片上传
  const handleContentUpload = async (options: any) => {
    const { file } = options;
    // 构造请求参数
    const params: API.uploadFileParams = {
      uploadFileRequest: {
        biz: 'note_md',
        bizId: noteId as any,
      },
    };
    // 使用 FormData 包装文件数据
    const formData = new FormData();
    formData.append('file', file);
    try {
      const res = await uploadFile(params, formData);
      if (res.data) {
        setPicList((prevList) => [res.data || '', ...prevList]);
      }
      setShowPic(true);
      message.success('图片上传成功');
    } catch (e: any) {
      message.error('图片上传失败，' + e.message);
    }
  };

  return (
    <div className="max-width-content">
      <Card
        title={!isEdit ? '查看笔记' : '更新笔记'}
        extra={
          <div>
            {!isEdit ? (
              <Button onClick={() => setIsEdit(true)}>修改笔记</Button>
            ) : (
              <Button onClick={() => setIsEdit(false)}>查看详情</Button>
            )}
          </div>
        }
      >
        {!isEdit ? (
          <div>
            <Card
              loading={loading}
              actions={[
                <div key="fire">
                  <FireOutlined />
                  {note.viewNum || '浏览'}
                </div>,
                <div key="like">
                  <LikeOutlined />
                  {note.thumbNum || '点赞'}
                </div>,
                <div key="heart">
                  <HeartOutlined />
                  {note.favourNum || '收藏'}
                </div>,
              ]}
            >
              <Flex justify="space-between" align="center">
                <div>
                  <Title level={3} style={{ fontSize: 20 }}>
                    {`个人笔记 - ${note?.title}`}
                  </Title>
                  <TagList tagList={note.tagList} />
                </div>
                <Image src={note.picture || '/xiaoxin.jpg'} height={100} />
              </Flex>
            </Card>
            <div style={{ marginBottom: 16 }} />
            <Card title="笔记内容" loading={loading}>
              <MdViewer value={note.content} />
            </Card>
            <div style={{ marginBottom: 16 }} />
            <Card title="其他信息" loading={loading}>
              <ProDescriptions
                column={{
                  xs: 1, // 小屏幕每行1列
                  sm: 1,
                  md: 2, // 中等及以上屏幕每行2列
                  lg: 2,
                  xl: 2,
                }}
                columns={otherColumns}
                dataSource={note}
              />
            </Card>
            <div style={{ marginBottom: 16 }} />
            <Card title="审核状态" loading={loading}>
              <ProDescriptions
                column={{
                  xs: 1, // 小屏幕每行1列
                  sm: 1,
                  md: 2, // 中等及以上屏幕每行2列
                  lg: 2,
                  xl: 2,
                }}
                columns={reviewColumns}
                dataSource={note}
              />
            </Card>
          </div>
        ) : (
          <div>
            <ProForm
              size="middle"
              style={{ width: '95%', margin: 'auto' }}
              formRef={formRef}
              onFinish={async (value) => {
                await onSubmitFrom(value, tags);
              }}
            >
              <ProFormText
                label="笔记标题："
                name="title"
                rules={[{ required: true, message: '请输入笔记标题' }]}
                initialValue={note.title}
              />
              <ProForm.Item name="picture" label="笔记封面">
                {noteAvatar ? <Image src={noteAvatar} height={120} /> : <></>}
                <div style={{ marginBottom: 16 }} />
                <Input value={`图片链接：${noteAvatar}`} disabled={true} />
                <div style={{ marginBottom: 16 }} />
                <Upload showUploadList={false} customRequest={handleAvatarUpload}>
                  <div>
                    <Button size="small">
                      <UploadOutlined />
                      更换头像
                    </Button>
                  </div>
                </Upload>
              </ProForm.Item>
              <ProFormSelect
                name="categoriesList"
                label="所属版块"
                mode="multiple"
                options={categoriesListForm}
                initialValue={categoriesIdList}
              />
              <ProFormText
                label="笔记内容："
                name="content"
                extra={
                  <div>
                    <Space>
                      {showPic ? (
                        <Button size="middle" type="primary" onClick={() => setShowPic(false)}>
                          隐藏图片
                        </Button>
                      ) : (
                        <Button size="middle" type="primary" onClick={() => setShowPic(true)}>
                          查看图片
                        </Button>
                      )}
                      <Upload showUploadList={false} customRequest={handleContentUpload}>
                        <div>
                          <Button size="small">
                            <UploadOutlined />
                            添加图片
                          </Button>
                        </div>
                      </Upload>
                    </Space>
                    {showPic ? (
                      <div>
                        <ProDescriptions column={1}>
                          {picList.map((pic, key) => {
                            return (
                              <ProDescriptions.Item key={key} copyable={true} valueType="text">
                                {`![](${pic})`}
                              </ProDescriptions.Item>
                            );
                          })}
                        </ProDescriptions>
                      </div>
                    ) : (
                      <></>
                    )}
                  </div>
                }
              >
                <MdEditor
                  value={contentMd}
                  onChange={(value) => {
                    formRef.current?.setFieldsValue({ content: value });
                    setContentMd(value);
                  }}
                />
              </ProFormText>
              <ProFormRadio.Group
                name="visible"
                label="可见范围："
                radioType="button"
                initialValue={note.visible}
                options={[
                  {
                    label: '公开',
                    value: 0,
                  },
                  {
                    label: '仅自己可见',
                    value: 1,
                  },
                ]}
              />
              <ProFormSwitch
                label="是否置顶"
                name="isTop"
                checkedChildren="置顶"
                unCheckedChildren="不置顶"
                initialValue={note.isTop !== 0}
              />
              <ProForm.Item label="笔记标签：" name="tags">
                {tags.map((tag) => (
                  <Tag
                    onChange={() => formRef.current?.setFieldsValue({ tags: tags })}
                    key={tag}
                    closable
                    onClose={() => handleClose(tag)}
                  >
                    {tag}
                  </Tag>
                ))}
                <Input
                  size="small"
                  value={inputValue}
                  onChange={(e) => {
                    setInputValue(e.target.value);
                    formRef.current?.setFieldsValue({ tags: tags });
                  }}
                  onPressEnter={handlePressEnter}
                  placeholder="输入后回车添加标签"
                  style={{ width: 'auto', marginTop: 8 }}
                />
              </ProForm.Item>
            </ProForm>
          </div>
        )}
      </Card>
      <NoteShareModal
        note={note}
        modalVisible={shareModalVisible}
        onCancel={() => {
          setShareModalVisible(false);
        }}
      />
    </div>
  );
};

export default NotePersonalPage;
