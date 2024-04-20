import {PageContainer} from '@ant-design/pro-components';
import {history, Outlet, useLocation, useMatch} from '@umijs/max';
import {Descriptions, DescriptionsProps} from 'antd';
import type {FC} from 'react';

type SearchProps = {
  children?: React.ReactNode;
};

const tabList = [
  {
    key: 'true_or_false',
    tab: '判断题',
  },
  {
    key: 'choice_question',
    tab: '选择题',
  },
  {
    key: 'question',
    tab: '编程题',
  },
];

const Search: FC<SearchProps> = () => {
  const location = useLocation();
  let match = useMatch(location.pathname);
  const searchParams = new URLSearchParams(location.search);
  const id = searchParams.get('id');

  const getId = () => {
    const searchParams = new URLSearchParams(location.search);
    console.log(searchParams.get('id'))
    return searchParams.get('id')
  }
  const handleTabChange = (key: string) => {
    const url =
      match?.pathname === '/' ? '' : match?.pathname.substring(0, match.pathname.lastIndexOf('/'));
    switch (key) {
      case 'true_or_false':
        history.push(`${url}/true_or_false/${id}`);
        break;
      case 'choice_question':
        history.push(`${url}/choice_question/${id}`);
        break;
      case 'question':
        history.push(`${url}/question/${id}`);
        break;
      default:
        break;
    }
  };

  const items: DescriptionsProps['items'] = [
    {
      key: '1',
      label: 'UserName',
      children: <p>Zhou Maomao</p>,
    },
    {
      key: '2',
      label: 'Telephone',
      children: <p>1810000000</p>,
    },
    {
      key: '3',
      label: 'Live',
      children: <p>Hangzhou, Zhejiang</p>,
    },
    {
      key: '4',
      label: 'Remark',
      children: <p>empty</p>,
    },
    {
      key: '5',
      label: 'Address',
      children: <p>No. 18, Wantang Road, Xihu District, Hangzhou, Zhejiang, China</p>,
    },
  ];

  const getTabKey = () => {
    const tabKey = location.pathname.substring(location.pathname.lastIndexOf('/') + 1);
    if (tabKey && tabKey !== '/') {
      return tabKey;
    }
    return 'articles';
  };

  return (
    <PageContainer
      tabList={tabList}
      tabActiveKey={getTabKey()}
      onTabChange={handleTabChange}
    >
      <Outlet />
      <Descriptions title="试卷信息" items={items} />;
    </PageContainer>

  );
};

export default Search;
