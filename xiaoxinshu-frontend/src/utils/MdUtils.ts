import { TreeDataNode } from 'antd';

/**
 * 提取 Markdown 中的标题
 * @param markdown
 */
// 将 markdown 转换为树形结构
const extractHeadingsAsTreeData = (markdown: string): TreeDataNode[] => {
  const headingRegex = /^(#{1,6})\s+(.*)/gm;
  const headings: TreeDataNode[] = [];
  const stack: { level: number; node: TreeDataNode }[] = [];
  let match;

  while ((match = headingRegex.exec(markdown)) !== null) {
    const level = match[1].length; // `#` 的数量表示标题级别
    const text = match[2];
    const key = `user-content-${text.trim().toLowerCase().replace(/\s+/g, '-')}`; // 生成唯一的 key
    const newNode: TreeDataNode = { title: text, key, children: [] };

    // 如果堆栈为空或当前标题级别小于等于堆栈顶的级别，出栈到合适的位置
    while (stack.length > 0 && stack[stack.length - 1].level >= level) {
      stack.pop();
    }

    // 如果堆栈非空，将新节点添加为堆栈顶节点的子节点
    if (stack.length > 0) {
      const parentNode = stack[stack.length - 1].node;
      if (!parentNode.children) parentNode.children = [];
      parentNode.children.push(newNode);
    } else {
      // 否则，这是一个顶层节点，直接添加到根数组
      headings.push(newNode);
    }

    // 将当前节点入栈
    stack.push({ level, node: newNode });
  }

  return headings;
};

/**
 * 为 Markdown 内容生成对应的锚点
 * @param markdown
 */
const addHeadingIds = (markdown: string) => {
  if (!markdown) {
    return '';
  }
  return markdown.replace(/^(#{1,6})\s+(.*)/gm, (_, hashes, text) => {
    const id = text.trim().toLowerCase().replace(/\s+/g, '-');
    return `${hashes} <span id="${id}"></span>${text}`;
  });
};

export default { extractHeadingsAsTreeData, addHeadingIds };
