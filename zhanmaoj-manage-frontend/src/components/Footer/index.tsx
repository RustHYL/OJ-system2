import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import React from 'react';


const defaultMessage = '猫五郎单人技术出品';
const currentYear = new Date().getFullYear();
const Footer: React.FC = () => {
  return (
      <DefaultFooter
          copyright={`${currentYear} ${defaultMessage}`}
          links={[
            {
              key: 'zhnma练题',
              title: 'zhanma-练题',
              href: 'http://localhost:8080/',
              blankTarget: true,
            },
            {
              key: 'Ant Deign',
              title: 'Ant Deign',
              href: 'https://ant.design',
              blankTarget: true,
            },
            {
              key: 'github',
              title: <><GithubOutlined />猫五郎 GitHub</>,
              href: 'https://github.com/RustHYL',
              blankTarget: true,
            },
          ]}
      />
  );
};

export default Footer;
