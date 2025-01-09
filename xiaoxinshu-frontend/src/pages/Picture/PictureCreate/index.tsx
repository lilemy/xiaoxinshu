import {
  editPicture,
  getPictureVoById,
  listPictureTagCategory,
  uploadPicture,
  uploadPictureByUrl,
} from '@/services/xiaoxinshu/pictureController';
import { history, useParams } from '@@/exports';
import { LoadingOutlined, PlusOutlined } from '@ant-design/icons';
import { ProFormSelect, ProFormText, ProFormTextArea } from '@ant-design/pro-components';
import { ProForm, ProFormInstance } from '@ant-design/pro-form/lib';
import { Card, GetProp, message, Tabs, Typography, Upload, UploadProps } from 'antd';
import Search from 'antd/es/input/Search';
import React, { useEffect, useRef, useState } from 'react';
import './index.css';

type FileType = Parameters<GetProp<UploadProps, 'beforeUpload'>>[0];

const beforeUpload = (file: FileType) => {
  const isJpgOrPng =
    file.type === 'image/jpeg' ||
    file.type === 'image/png' ||
    file.type === 'image/jpg' ||
    file.type === 'image/webp';
  if (!isJpgOrPng) {
    message.error('不支持上传该格式图片，推荐 jpg 或 png').then(() => {
      return;
    });
  }
  const isLt5M = file.size / 1024 / 1024 < 5;
  if (!isLt5M) {
    message.error('不能上传上过 5M 的图片').then(() => {
      return;
    });
  }
  return isJpgOrPng && isLt5M;
};

const PictureCreatePage: React.FC = () => {
  const params = useParams();
  const { pictureId, spaceId } = params;
  const [imageId, setImageId] = useState<any>(pictureId ?? '');
  const [loading, setLoading] = useState<boolean>(false);
  const formRef = useRef<ProFormInstance>();
  const [image, setImage] = useState<API.PictureVO>();
  const [tag, setTag] = useState<string[]>([]);
  const [category, setCategory] = useState<string[]>([]);
  // 上传图片
  const handleUpload = async ({ file }: any) => {
    setLoading(true);
    try {
      // 使用 FormData 包装文件数据
      const formData = new FormData();
      formData.append('file', file);
      const res = await uploadPicture(
        { pictureUploadRequest: { id: imageId, spaceId: spaceId as any } },
        formData,
      );
      setImage(res.data);
      setImageId(res.data?.id);
      message.success('图片上传成功');
      formRef.current?.resetFields();
    } catch (e: any) {
      message.error('图片上传失败：' + e.message);
    }
    setLoading(false);
  };
  // 上传图片
  const handleUploadByUrl = async (fileUrl: string) => {
    setLoading(true);
    try {
      const res = await uploadPictureByUrl({
        id: imageId,
        spaceId: spaceId as any,
        fileUrl: fileUrl,
      });
      setImage(res.data);
      setImageId(res.data?.id);
      message.success('图片上传成功');
      formRef.current?.resetFields();
    } catch (e: any) {
      message.error('图片上传失败：' + e.message);
    }
    setLoading(false);
  };
  // 当 id 存在时加载图片信息
  const loadPicture = async () => {
    setLoading(true);
    if (!pictureId) {
      message.error('图片不存在！');
      return;
    }
    try {
      const res = await getPictureVoById({ id: pictureId as any });
      setImage(res.data);
      setImageId(res.data?.id);
      formRef.current?.resetFields();
    } catch (e: any) {
      message.error('获取图片信息失败：' + e.message);
    }
    setLoading(false);
  };
  // 加载图片分类标签信息
  const loadTagAndCategory = async () => {
    setLoading(true);
    try {
      const res = await listPictureTagCategory();
      setTag(res.data?.tagList ?? []);
      setCategory(res.data?.categoryList ?? []);
      formRef.current?.resetFields();
    } catch (e: any) {
      message.error('获取图片分类标签信息失败：' + e.message);
    }
    setLoading(false);
  };
  useEffect(() => {
    loadTagAndCategory().then();
    if (imageId) {
      // 只有当 imageId 有值时执行方法
      loadPicture().then();
    }
  }, []);
  const categoryListForm = category.map((category) => ({
    value: category,
    label: category,
  }));
  const tagListForm = tag.map((tag) => ({
    value: tag,
    label: tag,
  }));
  const onSubmitFrom = async (value: API.PictureEditRequest) => {
    setLoading(true);
    try {
      await editPicture({
        id: image?.id,
        ...value,
      });
      if (spaceId) {
        message.success('图片保存成功');
        formRef.current?.resetFields();
        history.push(`/pictures/my/space/${spaceId}`);
      } else {
        message.success('图片上传成功，等待管理员审核');
        formRef.current?.resetFields();
        history.push(`/picture/${image?.id}`);
      }
    } catch (e: any) {
      message.error('创建失败，' + e.message);
    }
    setLoading(false);
  };
  const tabItems = [
    {
      key: 'file',
      label: '文件上传',
      children: (
        <div className="picture-upload">
          <Upload
            listType="picture-card"
            showUploadList={false}
            beforeUpload={beforeUpload}
            customRequest={handleUpload}
          >
            {image?.url ? (
              <img src={image.url} alt="picture" />
            ) : (
              <button style={{ border: 0, background: 'none' }} type="button">
                {loading ? <LoadingOutlined /> : <PlusOutlined />}
                <div style={{ marginTop: 8 }}>点击或拖动图片上传</div>
              </button>
            )}
          </Upload>
        </div>
      ),
    },
    {
      key: 'url',
      label: 'URL 上传',
      children: (
        <div className="picture-upload-url">
          <Search
            size="large"
            type="primary"
            loading={loading}
            onSearch={handleUploadByUrl}
            placeholder="请输入图片 URL"
            enterButton="提交"
            style={{ width: '100%', marginTop: 20 }}
          />
          <div className="img-div">{image?.url && <img src={image.url} alt="picture" />}</div>
        </div>
      ),
    },
  ];
  return (
    <div className="max-width-content">
      <Card title={image ? '更新图片' : '创建图片'}>
        {spaceId && (
          <Typography.Paragraph type="secondary">
            保存至空间：
            <a href={`/pictures/my/space/${spaceId}`} target="_blank" rel="noreferrer">
              {spaceId}
            </a>
          </Typography.Paragraph>
        )}
        <Tabs items={tabItems} />
        {image && (
          <ProForm
            size="large"
            style={{ width: '95%', margin: '20px auto 5px' }}
            formRef={formRef}
            onFinish={async (value) => {
              await onSubmitFrom(value);
            }}
          >
            <ProFormText
              label="图片名称："
              name="name"
              rules={[{ required: true, message: '请输入图片名称！' }]}
              initialValue={image.name}
              fieldProps={{
                placeholder: '请输入图片名称',
              }}
            />
            <ProFormTextArea
              label="图片简介："
              name="introduction"
              initialValue={image.introduction}
              fieldProps={{
                placeholder: '请输入图片简介',
              }}
            />
            <ProFormSelect
              label="图片分类："
              name="category"
              options={categoryListForm}
              initialValue={image.category}
              fieldProps={{
                placeholder: '请选择图片分类',
              }}
            />
            <ProFormSelect
              label="图片标签："
              name="tags"
              options={tagListForm}
              initialValue={image.tags}
              mode="multiple"
              fieldProps={{
                mode: 'tags', // 启用 tags 模式
                placeholder: '请选择或输入标签',
              }}
            />
          </ProForm>
        )}
      </Card>
    </div>
  );
};

export default PictureCreatePage;
