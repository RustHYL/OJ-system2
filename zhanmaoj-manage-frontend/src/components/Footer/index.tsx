import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import React from 'react';


const defaultMessage = '黄雨龙单人技术出品';
const currentYear = new Date().getFullYear();
const Footer: React.FC = () => {
  return (
      <DefaultFooter
          copyright={`${currentYear} ${defaultMessage}`}
          links={[
            {
              key: 'gege',
              title: '只因o',
              href: 'https://pro.ant.design',
              blankTarget: true,
            },
            {
              key: 'Beautiful',
              title: '太美',
              href: 'https://ant.design',
              blankTarget: true,
            },
            {
              key: 'github',
              title: <><GithubOutlined />黄雨龙 GitHub</>,
              href: 'https://github.com/RustHYL',
              blankTarget: true,
            },
          ]}
      />
  );
};

export default Footer;
