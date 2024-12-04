import { useNavigate } from '@umijs/max';
import { Input } from 'antd';
import React, { useEffect, useState } from 'react';

const SearchPage: React.FC = () => {
  const [searchValue, setSearchValue] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search); // 获取查询字符串
    const valueFromUrl = urlParams.get('value'); // 获取 value 参数的值
    setSearchValue(valueFromUrl || '');
  }, []); // 只在组件挂载时运行一次

  return (
    <div
      className="max-width-content"
      style={{
        textAlign: 'center',  // 水平居中
        marginTop: '5vh',    // 设置容器的顶部间距
      }}
    >
      <Input.Search
        style={{ maxWidth: '800px' }}
        placeholder="请输入搜索关键词"
        allowClear
        size="large"
        onSearch={(value) => {
          setSearchValue(value);
          navigate(`/search?value=${value}`);
        }}
        enterButton="搜索"
      />
      <br/>
      {searchValue}
    </div>
  );
};

export default SearchPage;
