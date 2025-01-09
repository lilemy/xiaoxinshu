export default [
  {
    path: '/user',
    layout: false,
    routes: [
      { name: '登录', path: '/user/login', component: './User/Login' },
      {
        name: '注册',
        path: '/user/register',
        component: './User/Register',
      },
    ],
  },
  { path: '/welcome', name: '欢迎', icon: 'smile', component: './Welcome' },
  { path: '/search', name: '搜索', component: './Search', hideInMenu: true },
  { path: '/banks', name: '题库', icon: 'table', component: './Bank' },
  {
    path: '/bank/:questionBankId',
    name: '题库详情',
    component: './Bank/BankDetail',
    hideInMenu: true,
  },
  {
    path: '/bank/:questionBankId/question/:questionId',
    name: '题目详情',
    component: './Bank/BankQuestionDetail',
    hideInMenu: true,
  },
  { path: '/questions', name: '题目', icon: 'bars', component: './Question' },
  {
    path: '/question/:questionId',
    name: '题目详情',
    component: './Question/QuestionDetail',
    hideInMenu: true,
  },
  {
    path: '/question/personal/:questionId',
    name: '个人题目详情',
    component: './Question/QuestionPersonal',
    hideInMenu: true,
  },
  {
    path: '/question/create',
    name: '创建题目',
    component: './Question/QuestionCreate',
    hideInMenu: true,
  },
  {
    path: '/notes',
    name: '笔记',
    icon: 'read',
    component: './Note',
  },
  {
    path: '/note/:noteId',
    name: '笔记详情',
    component: './Note/NoteDetail',
    hideInMenu: true,
  },
  {
    path: '/note/create',
    name: '创建笔记',
    component: './Note/NoteCreate',
    hideInMenu: true,
  },
  {
    path: '/note/personal/:noteId',
    name: '个人笔记详情',
    component: './Note/NotePersonal',
    hideInMenu: true,
  },
  {
    path: '/pictures',
    name: '图片',
    icon: 'picture',
    routes: [
      { path: '/pictures', redirect: '/pictures/public' },
      {
        path: '/pictures/public',
        name: '公共图库',
        component: './Picture',
        icon: 'picture',
      },
      {
        path: '/pictures/my/space/:spaceId',
        name: '我的空间',
        component: './Space/MySpace',
        icon: 'user',
      },
    ],
  },
  {
    path: '/picture/create',
    name: '上传图片',
    component: './Picture/PictureCreate',
    hideInMenu: true,
  },
  {
    path: '/picture/create/:spaceId',
    name: '上传空间图片',
    component: './Picture/PictureCreate',
    hideInMenu: true,
  },
  {
    path: '/picture/edit/:pictureId',
    name: '修改图片信息',
    component: './Picture/PictureCreate',
    hideInMenu: true,
  },
  {
    path: '/picture/edit/:pictureId/:spaceId',
    name: '修改空间图片信息',
    component: './Picture/PictureCreate',
    hideInMenu: true,
  },
  {
    path: '/picture/:pictureId',
    name: '图片详情',
    component: './Picture/PictureDetail',
    hideInMenu: true,
  },
  {
    path: '/space/create',
    name: '创建空间',
    component: './Space/SpaceCreate',
    hideInMenu: true,
  },
  {
    path: '/interfaces',
    name: '接口',
    component: './Interface',
    icon: 'api',
  },
  {
    path: '/interface/:interfaceInfoId',
    name: '接口信息详情',
    component: './Interface/InterfaceDetail',
    hideInMenu: true,
  },
  {
    path: '/account/center',
    name: '个人中心',
    component: './Account/Center',
    hideInMenu: true,
  },
  {
    path: '/account/setting',
    name: '个人设置',
    component: './Account/Setting',
    hideInMenu: true,
  },
  {
    path: '/account/accessKey',
    name: '接口密钥',
    component: './Account/AccessKey',
    hideInMenu: true,
  },
  {
    path: '/admin',
    name: '管理页',
    icon: 'crown',
    access: 'canAdmin',
    routes: [
      { path: '/admin', redirect: '/admin/users' },
      { path: '/admin/users', name: '用户管理', component: './Admin/User', icon: 'user' },
      { path: '/admin/banks', name: '题库管理', component: './Admin/QuestionBank', icon: 'table' },
      {
        path: '/admin/questions',
        name: '题目管理',
        component: './Admin/Question',
        icon: 'bars',
      },
      {
        path: '/admin/review/questions',
        name: '题目审核',
        component: './Admin/Review/Question',
        hideInMenu: true,
      },
      {
        path: '/admin/picture',
        name: '图片管理',
        component: './Admin/Picture',
        icon: 'picture',
      },
      {
        path: '/admin/picture/batchAdd',
        name: '批量添加图片',
        component: './Admin/Picture/PictureBatchAdd',
        hideInMenu: true,
      },
      { path: '/admin/space', name: '空间管理', component: './Admin/Space', icon: 'folder' },
      { path: '/admin/note', name: '笔记管理', component: './Admin/Note', icon: 'read' },
      {
        path: '/admin/review/notes',
        name: '笔记审核',
        component: './Admin/Review/Note',
        hideInMenu: true,
      },
      {
        path: '/admin/categories',
        name: '笔记分类管理',
        component: './Admin/Categories',
        icon: 'fileSearch',
      },
      {
        path: '/admin/interfaceInfos',
        name: '接口信息管理',
        component: './Admin/InterfaceInfo',
        icon: 'api',
      },
      {
        path: '/admin/userInterfaceInfos',
        name: '用户接口关系管理',
        component: './Admin/UserInterfaceInfo',
        icon: 'database',
      },
    ],
  },
  { path: '/', redirect: '/welcome' },
  { path: '*', layout: false, component: './404' },
];
