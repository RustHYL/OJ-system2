import React, { useState } from "react";
import JudgeCase from "@/components/JudgeCaseList";


const App: React.FC = () => {
    const [JudgeCases, setJudgeCases] = useState<{ input: string; output: string }[]>([]);

    const handleAddJudgeCase = (JudgeCase: { input: string; output: string }) => {
        setJudgeCases([...JudgeCases, JudgeCase]);
        console.log(JSON.stringify(JudgeCases))
    };

    return (
        <div>
            <h1>Input/Output List</h1>
            <JudgeCase onAddJudgeCase={handleAddJudgeCase} />
            <hr />
            <h2>Input/Output List:</h2>
            <ul>
                {JudgeCases.map((JudgeCase, index) => (
                    <li key={index}>
                        Input: {JudgeCase.input}, Output: {JudgeCase.output}
                        JudgeCases: {JSON.stringify(JudgeCases)}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default App;
