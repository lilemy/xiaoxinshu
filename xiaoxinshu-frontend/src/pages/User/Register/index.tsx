import {Footer} from '@/components';
import {LockOutlined, UserOutlined} from '@ant-design/icons';
import {LoginForm, ProFormText} from '@ant-design/pro-components';
import {Helmet, history} from '@umijs/max';
import {Card, Tabs, message} from 'antd';
import {createStyles} from 'antd-style';
import React, {useState} from 'react';
import Settings from '../../../../config/defaultSettings';
import {Link} from "@@/exports";
import {userRegister} from "@/services/xiaoxinshu/userController";

const useStyles = createStyles(({token}) => {
  return {
    action: {
      marginLeft: '8px',
      color: 'rgba(0, 0, 0, 0.2)',
      fontSize: '24px',
      verticalAlign: 'middle',
      cursor: 'pointer',
      transition: 'color 0.3s',
      '&:hover': {
        color: token.colorPrimaryActive,
      },
    },
    lang: {
      width: 42,
      height: 42,
      lineHeight: '42px',
      position: 'fixed',
      right: 16,
      borderRadius: token.borderRadius,
      ':hover': {
        backgroundColor: token.colorBgTextHover,
      },
    },
    container: {
      display: 'flex',
      flexDirection: 'column',
      height: '100vh',
      overflow: 'auto',
      backgroundImage:
        "url('https://campus-group.oss-cn-chengdu.aliyuncs.com/register_img.png')",
      backgroundSize: '100% 100%',
    },
  };
});
const Register: React.FC = () => {
  const [type, setType] = useState<string>('account');
  const {styles} = useStyles();
  const handleSubmit = async (values: API.UserRegisterRequest) => {
    const {userPassword, checkPassword} = values;
    // 校验
    if (userPassword !== checkPassword) {
      message.error('两次输入的密码不一致');
      return;
    }

    try {
      // 登录
      const id = await userRegister(values);
      if (id.code === 0 && id) {
        const defaultRegisterSuccessMessage = '注册成功！';
        message.success(defaultRegisterSuccessMessage);
        history.push({
          pathname: '/user/login',
        })
      }
    } catch (error: any) {
      const defaultRegisterFailureMessage = `注册失败: ${error.message}`;
      console.log(error);
      message.error(defaultRegisterFailureMessage);
    }
  };
  return (
    <div className={styles.container}>
      <Helmet>
        <title>
          {'注册'}- {Settings.title}
        </title>
      </Helmet>
      <div
        style={{
          flex: '1',
          padding: '32px 0',
        }}
      >
        <Card style={{
          width: '460px',
          margin: 'auto',
          backgroundColor: 'rgba(255, 255, 255, 0.7)',
          marginTop:'10px'
        }}>
          <LoginForm
            submitter={{
              searchConfig: {
                submitText: '注册'
              }
            }}
            contentStyle={{
              minWidth: 280,
              maxWidth: '75vw',
            }}
            logo={<img alt="logo" src="/logo.svg"/>}
            title="小新书"
            subTitle={''}
            onFinish={async (values) => {
              await handleSubmit(values as API.UserRegisterRequest);
            }}
          >
            <Tabs
              activeKey={type}
              onChange={setType}
              centered
              items={[
                {
                  key: 'account',
                  label: '账户密码注册',
                },
              ]}
            />

            {type === 'account' && (
              <>
                <ProFormText
                  name="userAccount"
                  fieldProps={{
                    size: 'large',
                    prefix: <UserOutlined/>,
                  }}
                  placeholder={'账号: '}
                  rules={[
                    {
                      required: true,
                      message: '账号是必填项！',
                    },
                  ]}
                />
                <ProFormText.Password
                  name="userPassword"
                  fieldProps={{
                    size: 'large',
                    prefix: <LockOutlined/>,
                  }}
                  placeholder={'密码: '}
                  rules={[
                    {
                      required: true,
                      message: '密码是必填项！',
                    },
                  ]}
                />
                <ProFormText.Password
                  name="checkPassword"
                  fieldProps={{
                    size: 'large',
                    prefix: <LockOutlined/>,
                  }}
                  placeholder={'确认密码: '}
                  rules={[
                    {
                      required: true,
                      message: '确认密码是必填项！',
                    },
                  ]}

                />
              </>
            )}
            <div
              style={{
                marginBottom: 24,
                textAlign: 'right',
              }}
            >
              <Link to="/user/login">去登录</Link>
            </div>
          </LoginForm>
        </Card>

      </div>
      <Footer/>
    </div>
  );
};
export default Register;
