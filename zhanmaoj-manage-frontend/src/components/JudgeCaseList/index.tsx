import React, { useState } from "react";
import { Input, Button, List } from "antd";

interface JudgeCaseProps {
    onAddJudgeCase: (judgeCase: { id: number; input: string; output: string }) => void;
}

const JudgeCase: React.FC<JudgeCaseProps> = ({ onAddJudgeCase }) => {
    const [input, setInput] = useState("");
    const [output, setOutput] = useState("");
    const [judgeCases, setJudgeCases] = useState<{ id: number; input: string; output: string }[]>([]);
    const [nextId, setNextId] = useState(1); // 用于生成唯一ID的计数器

    const handleAddJudgeCase = () => {
        if (!input || !output) return; // 如果输入或输出为空，则不添加
        const newJudgeCase = { id: nextId, input, output };
        setJudgeCases([...judgeCases, newJudgeCase]);
        setInput("");
        setOutput("");
        setNextId(nextId + 1); // 更新ID计数器
        onAddJudgeCase(newJudgeCase);
    };

    const handleDeleteJudgeCase = (id: number) => {
        setJudgeCases(judgeCases.filter(jc => jc.id !== id)); // 删除对应ID的JudgeCase
    };

    const renderJudgeCaseItem = (judgeCase: { id: number; input: string; output: string }) => {
        return (
            <List.Item
                actions={[
                    <Button style={{ marginRight: 10 }} type="link" danger onClick={() => handleDeleteJudgeCase(judgeCase.id)}>
                        删除
                    </Button>,
                ]}
            >
                <List.Item.Meta
                    title={`Input: ${judgeCase.input}`}
                    description={`Output: ${judgeCase.output}`}
                />
            </List.Item>
        );
    };

    return (
        <div>
            <h2>JudgeCase</h2>
            <div>
                <Input
                    style={{ width: 200 }}
                    placeholder="Input"
                    value={input}
                    onChange={e => setInput(e.target.value)}
                />
                <Input
                    style={{ width: 200 }}
                    placeholder="Output"
                    value={output}
                    onChange={e => setOutput(e.target.value)}
                />
                <Button type="primary" onClick={handleAddJudgeCase}>
                    新增
                </Button>
            </div>
            <List
                itemLayout="horizontal"
                dataSource={judgeCases}
                renderItem={renderJudgeCaseItem}
            />
        </div>
    );
};

export default JudgeCase;
