import React, { useState } from 'react';

const QuestionCard = ({ quiz, questionIndex, onAnswerSelect }) => {
  const [selectedAnswer, setSelectedAnswer] = useState(null);

  const handleAnswerClick = (answer) => {
    setSelectedAnswer(answer);
    onAnswerSelect(questionIndex, answer);
  };

  const allAnswers = quiz.answers.map(a => a.text);

  return (
    <div className="p-6 border rounded-lg shadow-md w-full max-w-md">
      <h2
        className="mb-4 text-lg font-semibold"
        dangerouslySetInnerHTML={{ __html: quiz.questionText }}
      />
      <ul className="flex flex-col gap-2">
        {allAnswers.map((answer, index) => (
          <li
            key={index}
            onClick={() => handleAnswerClick(answer)}
            className={`p-2 border-2 rounded cursor-pointer ${

              selectedAnswer === answer ? "border-amber-300" : "border-gray-400" 
            }`}
            dangerouslySetInnerHTML={{ __html: answer }}
          />
        ))}
      </ul>
    </div>
  );
};

export default QuestionCard;
