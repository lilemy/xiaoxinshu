import gfm from '@bytemd/plugin-gfm';
import highlight from '@bytemd/plugin-highlight';
import { Editor } from '@bytemd/react';
import theme, { setTheme, themeList } from 'bytemd-plugin-theme';
import 'bytemd/dist/index.css';
import 'highlight.js/styles/vs.css';
import { useEffect } from 'react';
import './index.css';

interface Props {
  value?: string;
  onChange?: (v: string) => void;
  placeholder?: string;
}

const plugins = [
  gfm(),
  highlight(),
  theme({
    themeList,
  }),
];

/**
 * Markdown 编辑器
 * @param props
 * @constructor
 */
const MdEditor = (props: Props) => {
  const { value = '', onChange, placeholder } = props;
  // 设置默认主题
  useEffect(() => {
    setTheme('qklhk-chocolate');
  }, []);
  return (
    <div className="md-editor">
      <Editor
        value={value}
        placeholder={placeholder}
        mode="split"
        plugins={plugins}
        onChange={onChange}
      />
    </div>
  );
};

export default MdEditor;
