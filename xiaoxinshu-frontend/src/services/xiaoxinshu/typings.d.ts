declare namespace API {
  type BaseResponseBoolean = {
    code?: number;
    data?: boolean;
    message?: string;
  };

  type BaseResponseInteger = {
    code?: number;
    data?: number;
    message?: string;
  };

  type BaseResponseInterfaceInfo = {
    code?: number;
    data?: InterfaceInfo;
    message?: string;
  };

  type BaseResponseInterfaceInfoVO = {
    code?: number;
    data?: InterfaceInfoVO;
    message?: string;
  };

  type BaseResponseListInteger = {
    code?: number;
    data?: number[];
    message?: string;
  };

  type BaseResponseListInterfaceInfoByName = {
    code?: number;
    data?: InterfaceInfoByName[];
    message?: string;
  };

  type BaseResponseListQuestionBankListVO = {
    code?: number;
    data?: QuestionBankListVO[];
    message?: string;
  };

  type BaseResponseListSpaceLevel = {
    code?: number;
    data?: SpaceLevel[];
    message?: string;
  };

  type BaseResponseListUserByUserAccount = {
    code?: number;
    data?: UserByUserAccount[];
    message?: string;
  };

  type BaseResponseLoginUserVO = {
    code?: number;
    data?: LoginUserVO;
    message?: string;
  };

  type BaseResponseLong = {
    code?: number;
    data?: number;
    message?: string;
  };

  type BaseResponseNotePersonalVO = {
    code?: number;
    data?: NotePersonalVO;
    message?: string;
  };

  type BaseResponseNoteVO = {
    code?: number;
    data?: NoteVO;
    message?: string;
  };

  type BaseResponseObject = {
    code?: number;
    data?: Record<string, any>;
    message?: string;
  };

  type BaseResponsePageCategoriesVO = {
    code?: number;
    data?: PageCategoriesVO;
    message?: string;
  };

  type BaseResponsePageInterfaceInfo = {
    code?: number;
    data?: PageInterfaceInfo;
    message?: string;
  };

  type BaseResponsePageInterfaceInfoVO = {
    code?: number;
    data?: PageInterfaceInfoVO;
    message?: string;
  };

  type BaseResponsePageNote = {
    code?: number;
    data?: PageNote;
    message?: string;
  };

  type BaseResponsePageNotePersonalVO = {
    code?: number;
    data?: PageNotePersonalVO;
    message?: string;
  };

  type BaseResponsePageNoteVO = {
    code?: number;
    data?: PageNoteVO;
    message?: string;
  };

  type BaseResponsePagePicture = {
    code?: number;
    data?: PagePicture;
    message?: string;
  };

  type BaseResponsePagePictureVO = {
    code?: number;
    data?: PagePictureVO;
    message?: string;
  };

  type BaseResponsePageQuestion = {
    code?: number;
    data?: PageQuestion;
    message?: string;
  };

  type BaseResponsePageQuestionBank = {
    code?: number;
    data?: PageQuestionBank;
    message?: string;
  };

  type BaseResponsePageQuestionBankQuestionVO = {
    code?: number;
    data?: PageQuestionBankQuestionVO;
    message?: string;
  };

  type BaseResponsePageQuestionBankVO = {
    code?: number;
    data?: PageQuestionBankVO;
    message?: string;
  };

  type BaseResponsePageQuestionPersonalVO = {
    code?: number;
    data?: PageQuestionPersonalVO;
    message?: string;
  };

  type BaseResponsePageQuestionVO = {
    code?: number;
    data?: PageQuestionVO;
    message?: string;
  };

  type BaseResponsePageSpace = {
    code?: number;
    data?: PageSpace;
    message?: string;
  };

  type BaseResponsePageSpaceVO = {
    code?: number;
    data?: PageSpaceVO;
    message?: string;
  };

  type BaseResponsePageUser = {
    code?: number;
    data?: PageUser;
    message?: string;
  };

  type BaseResponsePageUserInterfaceInfo = {
    code?: number;
    data?: PageUserInterfaceInfo;
    message?: string;
  };

  type BaseResponsePageUserVO = {
    code?: number;
    data?: PageUserVO;
    message?: string;
  };

  type BaseResponsePicture = {
    code?: number;
    data?: Picture;
    message?: string;
  };

  type BaseResponsePictureTagCategory = {
    code?: number;
    data?: PictureTagCategory;
    message?: string;
  };

  type BaseResponsePictureVO = {
    code?: number;
    data?: PictureVO;
    message?: string;
  };

  type BaseResponseQuestionBankQuestionVO = {
    code?: number;
    data?: QuestionBankQuestionVO;
    message?: string;
  };

  type BaseResponseQuestionBankVO = {
    code?: number;
    data?: QuestionBankVO;
    message?: string;
  };

  type BaseResponseQuestionPersonalVO = {
    code?: number;
    data?: QuestionPersonalVO;
    message?: string;
  };

  type BaseResponseQuestionVO = {
    code?: number;
    data?: QuestionVO;
    message?: string;
  };

  type BaseResponseString = {
    code?: number;
    data?: string;
    message?: string;
  };

  type BaseResponseUser = {
    code?: number;
    data?: User;
    message?: string;
  };

  type BaseResponseUserByAccessKey = {
    code?: number;
    data?: UserByAccessKey;
    message?: string;
  };

  type BaseResponseUserVO = {
    code?: number;
    data?: UserVO;
    message?: string;
  };

  type CategoriesCreateRequest = {
    name?: string;
    priority?: number;
  };

  type CategoriesQueryRequest = {
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    id?: number;
    notId?: number;
    name?: string;
  };

  type CategoriesUpdateRequest = {
    id?: number;
    name?: string;
    priority?: number;
  };

  type CategoriesVO = {
    id?: number;
    name?: string;
    priority?: number;
    createTime?: string;
    updateTime?: string;
  };

  type DeleteRequest = {
    id?: number;
  };

  type getInterfaceInfoParams = {
    id: number;
  };

  type getInterfaceInfoVOParams = {
    id: number;
  };

  type getNotePersonalVOByIdParams = {
    id: number;
  };

  type getNoteVOByIdParams = {
    id: number;
  };

  type getPictureByIdParams = {
    id: number;
  };

  type getPictureVOByIdParams = {
    id: number;
  };

  type getQuestionBankQuestionVOByIdParams = {
    id: number;
  };

  type getQuestionBankVOByIdParams = {
    id: number;
  };

  type getQuestionPersonalByIdParams = {
    id: number;
  };

  type getQuestionVOByIdParams = {
    id: number;
  };

  type getUserByIdParams = {
    id: number;
  };

  type getUserSignInRecordParams = {
    year: number;
  };

  type getUserVOByIdParams = {
    id: number;
  };

  type InterfaceInfo = {
    id?: number;
    name?: string;
    description?: string;
    url?: string;
    actualUrl?: string;
    path?: string;
    requestParams?: string;
    requestHeader?: string;
    responseHeader?: string;
    status?: number;
    method?: string;
    userId?: number;
    createTime?: string;
    updateTime?: string;
    isDelete?: number;
  };

  type InterfaceInfoAddRequest = {
    name?: string;
    description?: string;
    url?: string;
    actualUrl?: string;
    path?: string;
    requestParams?: string;
    requestHeader?: string;
    responseHeader?: string;
    method?: string;
  };

  type InterfaceInfoByName = {
    id?: number;
    name?: string;
    description?: string;
  };

  type InterfaceInfoInvokeRequest = {
    id?: number;
    userRequestParams?: string;
  };

  type InterfaceInfoQueryRequest = {
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    id?: number;
    name?: string;
    description?: string;
    notId?: number;
    searchText?: string;
    url?: string;
    actualUrl?: string;
    path?: string;
    requestParams?: string;
    requestHeader?: string;
    responseHeader?: string;
    status?: number;
    method?: string;
    userId?: number;
  };

  type InterfaceInfoUpdateRequest = {
    id?: number;
    name?: string;
    description?: string;
    url?: string;
    actualUrl?: string;
    path?: string;
    requestParams?: string;
    requestHeader?: string;
    responseHeader?: string;
    method?: string;
  };

  type InterfaceInfoVO = {
    id?: number;
    name?: string;
    description?: string;
    url?: string;
    path?: string;
    requestParams?: string;
    requestHeader?: string;
    responseHeader?: string;
    status?: number;
    method?: string;
    userId?: number;
    createTime?: string;
    updateTime?: string;
  };

  type InterfaceOfflineRequest = {
    id?: number;
  };

  type InterfaceOnlineRequest = {
    id?: number;
  };

  type LoginUserVO = {
    id?: number;
    username?: string;
    userAccount?: string;
    userAvatar?: string;
    userProfile?: string;
    phone?: string;
    email?: string;
    userRole?: string;
    editTime?: string;
    updateTime?: string;
    createTime?: string;
  };

  type Note = {
    id?: number;
    title?: string;
    content?: string;
    tags?: string;
    picture?: string;
    thumbNum?: number;
    favourNum?: number;
    viewNum?: number;
    reviewStatus?: number;
    reviewMessage?: string;
    reviewerId?: number;
    reviewTime?: string;
    userId?: number;
    visible?: number;
    isTop?: number;
    editTime?: string;
    createTime?: string;
    updateTime?: string;
    isDelete?: number;
  };

  type NoteAddRequest = {
    title?: string;
    content?: string;
    tags?: string[];
    picture?: string;
    categoriesList?: number[];
  };

  type NoteCreateRequest = {
    title?: string;
    content?: string;
    tags?: string[];
    visible?: number;
    isTop?: number;
    categoriesIds?: number[];
  };

  type NoteEditRequest = {
    id?: number;
    title?: string;
    content?: string;
    tags?: string[];
    picture?: string;
    visible?: number;
    isTop?: number;
    categoriesList?: number[];
  };

  type NotePersonalVO = {
    id?: number;
    title?: string;
    content?: string;
    picture?: string;
    thumbNum?: number;
    favourNum?: number;
    viewNum?: number;
    visible?: number;
    isTop?: number;
    editTime?: string;
    createTime?: string;
    updateTime?: string;
    tagList?: string[];
    reviewStatus?: number;
    reviewMessage?: string;
    reviewerId?: number;
    reviewTime?: string;
    categoriesVOList?: CategoriesVO[];
  };

  type NoteQueryByCategoriesRequest = {
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    categoriesId?: number;
  };

  type NoteQueryRequest = {
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    id?: number;
    notId?: number;
    searchText?: string;
    title?: string;
    content?: string;
    tags?: string[];
    thumbNum?: number;
    favourNum?: number;
    viewNum?: number;
    userId?: number;
    visible?: number;
    needContent?: boolean;
    reviewStatus?: number;
    reviewMessage?: string;
    reviewerId?: number;
  };

  type NoteReviewRequest = {
    id?: number;
    reviewStatus?: number;
    reviewMessage?: string;
  };

  type NoteUpdateRequest = {
    id?: number;
    title?: string;
    content?: string;
    tags?: string[];
    picture?: string;
    categoriesList?: number[];
  };

  type NoteVO = {
    id?: number;
    title?: string;
    content?: string;
    picture?: string;
    thumbNum?: number;
    favourNum?: number;
    viewNum?: number;
    createTime?: string;
    updateTime?: string;
    tagList?: string[];
    userId?: number;
    user?: UserVO;
  };

  type OrderItem = {
    column?: string;
    asc?: boolean;
  };

  type PageCategoriesVO = {
    records?: CategoriesVO[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageCategoriesVO;
    searchCount?: PageCategoriesVO;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageInterfaceInfo = {
    records?: InterfaceInfo[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageInterfaceInfo;
    searchCount?: PageInterfaceInfo;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageInterfaceInfoVO = {
    records?: InterfaceInfoVO[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageInterfaceInfoVO;
    searchCount?: PageInterfaceInfoVO;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageNote = {
    records?: Note[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageNote;
    searchCount?: PageNote;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageNotePersonalVO = {
    records?: NotePersonalVO[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageNotePersonalVO;
    searchCount?: PageNotePersonalVO;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageNoteVO = {
    records?: NoteVO[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageNoteVO;
    searchCount?: PageNoteVO;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PagePicture = {
    records?: Picture[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PagePicture;
    searchCount?: PagePicture;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PagePictureVO = {
    records?: PictureVO[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PagePictureVO;
    searchCount?: PagePictureVO;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageQuestion = {
    records?: Question[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageQuestion;
    searchCount?: PageQuestion;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageQuestionBank = {
    records?: QuestionBank[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageQuestionBank;
    searchCount?: PageQuestionBank;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageQuestionBankQuestionVO = {
    records?: QuestionBankQuestionVO[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageQuestionBankQuestionVO;
    searchCount?: PageQuestionBankQuestionVO;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageQuestionBankVO = {
    records?: QuestionBankVO[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageQuestionBankVO;
    searchCount?: PageQuestionBankVO;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageQuestionPersonalVO = {
    records?: QuestionPersonalVO[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageQuestionPersonalVO;
    searchCount?: PageQuestionPersonalVO;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageQuestionVO = {
    records?: QuestionVO[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageQuestionVO;
    searchCount?: PageQuestionVO;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageSpace = {
    records?: Space[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageSpace;
    searchCount?: PageSpace;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageSpaceVO = {
    records?: SpaceVO[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageSpaceVO;
    searchCount?: PageSpaceVO;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageUser = {
    records?: User[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageUser;
    searchCount?: PageUser;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageUserInterfaceInfo = {
    records?: UserInterfaceInfo[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageUserInterfaceInfo;
    searchCount?: PageUserInterfaceInfo;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type PageUserVO = {
    records?: UserVO[];
    total?: number;
    size?: number;
    current?: number;
    orders?: OrderItem[];
    optimizeCountSql?: PageUserVO;
    searchCount?: PageUserVO;
    optimizeJoinOfCountSql?: boolean;
    maxLimit?: number;
    countId?: string;
    pages?: number;
  };

  type Picture = {
    id?: number;
    url?: string;
    originalUrl?: string;
    name?: string;
    introduction?: string;
    category?: string;
    tags?: string;
    picSize?: number;
    picWidth?: number;
    picHeight?: number;
    picScale?: number;
    picFormat?: string;
    userId?: number;
    spaceId?: number;
    reviewStatus?: number;
    reviewMessage?: string;
    reviewerId?: number;
    reviewTime?: string;
    createTime?: string;
    editTime?: string;
    updateTime?: string;
    isDelete?: number;
  };

  type PictureEditRequest = {
    id?: number;
    name?: string;
    introduction?: string;
    category?: string;
    tags?: string[];
  };

  type PictureQueryRequest = {
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    id?: number;
    name?: string;
    introduction?: string;
    category?: string;
    tags?: string[];
    picSize?: number;
    picWidth?: number;
    picHeight?: number;
    picScale?: number;
    picFormat?: string;
    searchText?: string;
    userId?: number;
    reviewStatus?: number;
    reviewMessage?: string;
    reviewerId?: number;
    spaceId?: number;
    nullSpaceId?: boolean;
  };

  type PictureReviewRequest = {
    id?: number;
    reviewStatus?: number;
    reviewMessage?: string;
  };

  type PictureTagCategory = {
    tagList?: string[];
    categoryList?: string[];
  };

  type PictureUpdateRequest = {
    id?: number;
    name?: string;
    introduction?: string;
    category?: string;
    tags?: string[];
  };

  type PictureUploadByBatchRequest = {
    searchText?: string;
    count?: number;
    namePrefix?: string;
  };

  type PictureUploadRequest = {
    id?: number;
    fileUrl?: string;
    picName?: string;
    spaceId?: number;
  };

  type PictureVO = {
    id?: number;
    url?: string;
    originalUrl?: string;
    name?: string;
    introduction?: string;
    tags?: string[];
    category?: string;
    picSize?: number;
    picWidth?: number;
    picHeight?: number;
    picScale?: number;
    picFormat?: string;
    userId?: number;
    createTime?: string;
    editTime?: string;
    updateTime?: string;
    user?: UserVO;
  };

  type Question = {
    id?: number;
    title?: string;
    content?: string;
    tags?: string;
    answer?: string;
    userId?: number;
    reviewStatus?: number;
    reviewMessage?: string;
    reviewerId?: number;
    reviewTime?: string;
    viewNum?: number;
    thumbNum?: number;
    favourNum?: number;
    priority?: number;
    editTime?: string;
    createTime?: string;
    updateTime?: string;
    isDelete?: number;
  };

  type QuestionAddRequest = {
    title?: string;
    content?: string;
    tags?: string[];
    answer?: string;
  };

  type QuestionBank = {
    id?: number;
    title?: string;
    description?: string;
    picture?: string;
    priority?: number;
    userId?: number;
    editTime?: string;
    createTime?: string;
    updateTime?: string;
    isDelete?: number;
  };

  type QuestionBankAddRequest = {
    title?: string;
    description?: string;
    picture?: string;
  };

  type QuestionBankListVO = {
    id?: number;
    title?: string;
    description?: string;
  };

  type QuestionBankQueryRequest = {
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    id?: number;
    notId?: number;
    searchText?: string;
    title?: string;
    description?: string;
    picture?: string;
    userId?: number;
    needQueryQuestionList?: boolean;
  };

  type QuestionBankQuestionAddRequest = {
    questionBankId?: number;
    questionId?: number;
  };

  type QuestionBankQuestionBatchRequest = {
    questionBankId?: number;
    questionIdList?: number[];
  };

  type QuestionBankQuestionQueryRequest = {
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    id?: number;
    notId?: number;
    questionBankId?: number;
    questionId?: number;
    userId?: number;
  };

  type QuestionBankQuestionRemoveRequest = {
    questionBankId?: number;
    questionId?: number;
  };

  type QuestionBankQuestionUpdateRequest = {
    id?: number;
    questionBankId?: number;
    questionId?: number;
  };

  type QuestionBankQuestionVO = {
    id?: number;
    questionBankId?: number;
    questionId?: number;
    userId?: number;
    createTime?: string;
    updateTime?: string;
    tagList?: string[];
    user?: UserVO;
  };

  type QuestionBankUpdateRequest = {
    id?: number;
    title?: string;
    description?: string;
    picture?: string;
    priority?: number;
  };

  type QuestionBankVO = {
    id?: number;
    title?: string;
    description?: string;
    picture?: string;
    userId?: number;
    createTime?: string;
    updateTime?: string;
    user?: UserVO;
  };

  type QuestionBatchDeleteRequest = {
    questionIdList?: number[];
  };

  type QuestionCreateRequest = {
    title?: string;
    questionBankIds?: number[];
    content?: string;
    tags?: string[];
    answer?: string;
  };

  type QuestionEditRequest = {
    id?: number;
    title?: string;
    questionBankList?: number[];
    content?: string;
    tags?: string[];
    answer?: string;
  };

  type QuestionPersonalVO = {
    id?: number;
    title?: string;
    content?: string;
    answer?: string;
    userId?: number;
    editTime?: string;
    createTime?: string;
    updateTime?: string;
    tagList?: string[];
    reviewStatus?: number;
    reviewMessage?: string;
    reviewerId?: number;
    reviewTime?: string;
    questionBankList?: QuestionBankListVO[];
  };

  type QuestionQueryRequest = {
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    id?: number;
    notId?: number;
    searchText?: string;
    title?: string;
    content?: string;
    tags?: string[];
    answer?: string;
    questionBankId?: number;
    userId?: number;
    needAnswer?: boolean;
    reviewStatus?: number;
    reviewMessage?: string;
    reviewerId?: number;
  };

  type QuestionReviewRequest = {
    id?: number;
    reviewStatus?: number;
    reviewMessage?: string;
  };

  type QuestionUpdateRequest = {
    id?: number;
    title?: string;
    content?: string;
    tags?: string[];
    priority?: number;
    answer?: string;
    reviewStatus?: number;
    reviewMessage?: string;
  };

  type QuestionVO = {
    id?: number;
    title?: string;
    content?: string;
    answer?: string;
    thumbNum?: number;
    favourNum?: number;
    viewNum?: number;
    userId?: number;
    createTime?: string;
    updateTime?: string;
    tagList?: string[];
    user?: UserVO;
  };

  type Space = {
    id?: number;
    spaceName?: string;
    spaceLevel?: number;
    maxSize?: number;
    maxCount?: number;
    totalSize?: number;
    totalCount?: number;
    userId?: number;
    createTime?: string;
    editTime?: string;
    updateTime?: string;
    isDelete?: number;
  };

  type SpaceCreateRequest = {
    spaceName?: string;
    spaceLevel?: number;
  };

  type SpaceEditRequest = {
    id?: number;
    spaceName?: string;
  };

  type SpaceLevel = {
    value?: number;
    text?: string;
    maxCount?: number;
    maxSize?: number;
  };

  type SpaceQueryRequest = {
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    id?: number;
    userId?: number;
    spaceName?: string;
    spaceLevel?: number;
  };

  type SpaceUpdateRequest = {
    id?: number;
    spaceName?: string;
    spaceLevel?: number;
    maxSize?: number;
    maxCount?: number;
  };

  type SpaceVO = {
    id?: number;
    spaceName?: string;
    spaceLevel?: number;
    maxSize?: number;
    maxCount?: number;
    totalSize?: number;
    totalCount?: number;
    userId?: number;
    createTime?: string;
    editTime?: string;
    updateTime?: string;
    user?: UserVO;
  };

  type uploadFileParams = {
    uploadFileRequest: UploadFileRequest;
  };

  type UploadFileRequest = {
    biz?: string;
    bizId?: number;
  };

  type uploadPictureParams = {
    pictureUploadRequest: PictureUploadRequest;
  };

  type User = {
    id?: number;
    userAccount?: string;
    userPassword?: string;
    unionId?: string;
    mpOpenId?: string;
    username?: string;
    userAvatar?: string;
    userProfile?: string;
    accessKey?: string;
    secretKey?: string;
    phone?: string;
    email?: string;
    userRole?: string;
    editTime?: string;
    createTime?: string;
    updateTime?: string;
    isDelete?: number;
  };

  type UserAddRequest = {
    userAccount?: string;
    userRole?: string;
    username?: string;
  };

  type UserByAccessKey = {
    accessKey?: string;
    secretKey?: string;
  };

  type UserByUserAccount = {
    id?: number;
    userAccount?: string;
  };

  type UserEditRequest = {
    id?: number;
    username?: string;
    userAvatar?: string;
    userProfile?: string;
    phone?: string;
    email?: string;
  };

  type UserInterfaceInfo = {
    id?: number;
    userId?: number;
    interfaceInfoId?: number;
    totalNum?: number;
    leftNum?: number;
    status?: number;
    createTime?: string;
    updateTime?: string;
    isDelete?: number;
  };

  type UserInterfaceInfoAddRequest = {
    userId?: number;
    interfaceInfoId?: number;
    leftNum?: number;
  };

  type UserInterfaceInfoQueryRequest = {
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    id?: number;
    userId?: number;
    interfaceInfoId?: number;
    status?: number;
  };

  type UserInterfaceInfoUpdateRequest = {
    id?: number;
    leftNum?: number;
  };

  type UserLoginRequest = {
    userAccount?: string;
    userPassword?: string;
  };

  type UserQueryRequest = {
    current?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    id?: number;
    username?: string;
    userAccount?: string;
    userRole?: string;
    phone?: string;
    email?: string;
  };

  type UserRegisterRequest = {
    userAccount?: string;
    userPassword?: string;
    checkPassword?: string;
  };

  type UserUpdateRequest = {
    id?: number;
    username?: string;
    userAccount?: string;
    userPassword?: string;
    userAvatar?: string;
    userProfile?: string;
    userRole?: string;
  };

  type UserVO = {
    id?: number;
    userAccount?: string;
    username?: string;
    userAvatar?: string;
    userProfile?: string;
    phone?: string;
    email?: string;
    userRole?: string;
    createTime?: string;
  };
}
