import React from 'react';
import {Tag} from "antd";

interface Props {
    tags: string[]; // 假设你的标签数组是这样的字符串数组
}

const TagsComponent: React.FC<Props> = ({ tags }) => {
    return (
        <div>
            {tags.map((tag, index) => (
                <Tag key={index} color="blue">
                    {tag}
                </Tag>
            ))}
        </div>
    );
};

export default TagsComponent;
