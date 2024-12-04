import { SearchOutlined } from '@ant-design/icons';
import { history } from '@umijs/max';
import { Input } from 'antd';
import './index.css';

export const SearchInput = () => {
  if (location.pathname.includes('/search')) {
    return <div></div>;
  }
  return (
    <div
      id="search-input"
      key="SearchOutlined"
      aria-hidden
      style={{
        display: 'flex',
        alignItems: 'center',
        marginInlineEnd: 24,
      }}
    >
      <Input.Search
        style={{
          borderRadius: 4,
          marginInlineEnd: 12,
        }}
        prefix={<SearchOutlined />}
        placeholder="站内搜索"
        variant="borderless"
        onSearch={(value) => {
          history.push(`/search?value=${value}`);
        }}
      />
    </div>
  );
};
