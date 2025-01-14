import React, { useState } from 'react';
import QuestionCard from './QuestionCard'; 

const QuizComponent = ({ quizData, onQuizComplete }) => {
  const [answers, setAnswers] = useState(
    quizData.map(() => ({
      text: null,
      trivaUUID: null,
      questionUUID: null,
    }))
  );

  const handleAnswerSelection = (questionIndex, answer) => {
    const updatedAnswers = [...answers];

    const question = quizData[questionIndex];

    updatedAnswers[questionIndex] = {
      ...updatedAnswers[questionIndex],
      text: answer,
      trivaUUID: question.trivaUUID,
      questionUUID: answer.questionUUID,
    };

    setAnswers(updatedAnswers);

    if (updatedAnswers.every((a) => a.text !== null)) {
      console.log(updatedAnswers);
      console.log(quizData);
      onQuizComplete(updatedAnswers); 
    }
  };

  return (
    <div className="flex flex-col items-center gap-8">
      {quizData.map((quiz, index) => (
        <QuestionCard
          key={quiz.questionId}
          quiz={quiz}
          questionIndex={index}
          onAnswerSelect={handleAnswerSelection}
        />
      ))}
    </div>
  );
};

export default QuizComponent;
