import React from 'react';

const ResultComponent = ({ resultData }) => {
  const { input } = resultData;

  const correctCount = input.filter(
    (item) => item.correctAnswer.text === item.userAnswer.text
  ).length;

  return (
    <div className="p-4">
      <h2 className="text-lg font-semibold mb-4">Quiz Results</h2>
      <p className="mb-4">
        You got <span className="font-bold">{correctCount}</span> out of{" "}
        <span className="font-bold">{input.length}</span> questions correct.
      </p>
      <div className="space-y-4">
        {input.map((item, index) => (
          <div
            key={item.questionId}
            className="p-4 border rounded shadow-sm"
          >
            <p className="font-medium">Question {index + 1}</p>
            <p>
              <span className="font-bold">Your Answer:</span>{" "}
              {item.userAnswer.text}
            </p>
            <p>
              <span className="font-bold">Correct Answer:</span>{" "}
              {item.correctAnswer.text}
            </p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ResultComponent;
