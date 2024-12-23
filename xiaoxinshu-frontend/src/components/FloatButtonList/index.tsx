import { FloatButton } from 'antd';
import React from 'react';

const FloatButtonList: React.FC = () => {
  return (
    <div>
      <FloatButton.Group>
        <FloatButton.BackTop visibilityHeight={0} />
      </FloatButton.Group>
    </div>
  );
};

export default FloatButtonList;
