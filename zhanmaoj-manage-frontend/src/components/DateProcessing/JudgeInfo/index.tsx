import React from 'react';

interface Props {
    judgeInfo: API.JudgeInfo; // 假设你的标签数组是这样的字符串数组
}

const JudgeInfoComponent: React.FC<Props> = ({ judgeInfo }) => {
    return (
        <div>
          <ul style={{ listStyle: 'none' , paddingLeft: 0}}>
            <li style={{textAlign: 'left'}}>
              <strong>Message:</strong> {judgeInfo.message}
            </li>
            <li style={{textAlign: 'left'}}>
              <strong>Memory:</strong> {judgeInfo.memory}Byte
            </li>
            <li style={{textAlign: 'left'}}>
              <strong>Time:</strong> {judgeInfo.time} ms
            </li>
          </ul>
        </div>
    );
};

export default JudgeInfoComponent;
