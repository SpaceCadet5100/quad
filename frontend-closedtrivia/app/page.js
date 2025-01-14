"use client"

import React, { useState, useEffect } from 'react';
import QuizComponent from './QuizComponent'; 
import ResultComponent from './ResultComponent'; 

const Home = () => {
  const [quizData, setQuizData] = useState(null);
  const [error, setError] = useState(null);
  const [resultData, setResultData] = useState(null);
  const [quizCompleted, setQuizCompleted] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch("http://localhost:8080/questions");
        if (!response.ok) {
          throw new Error("Failed to fetch data");
        }
        const data = await response.json();
        setQuizData(data);
      } catch (err) {
        setError(err.message);
      }
    };

    fetchData();
  }, []);

  async function postData(answers) {
    const response = await fetch("http://localhost:8080/checkanswers", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(answers),
    });
    return response.json();
  }

  const handleQuizCompletion = async (answers) => {
    try {
      const formattedAnswers = answers.map((answer, index) => {
        const question = quizData[index];
        return {
          questionId: question.questionId,
          triviaId: question.triviaId,
          userAnswer: answer,
        };
      });

      const data = await postData(formattedAnswers);
      setResultData(data);
      setQuizCompleted(true);
    } catch (err) {
      setError(err.message); 
    }
  };

  if (error) {
    return (
      <div className="flex flex-col items-center justify-center min-h-screen">
        <div className="p-6 border rounded-lg shadow-md max-w-md text-center">
          <h2 className="mb-4 text-lg font-semibold text-red-600">Error</h2>
          <p>{error}</p>
        </div>
      </div>
    );
  }

  if (!quizData) {
    return (
      <div className="flex flex-col items-center justify-center min-h-screen">
        <div className="loader">Loading...</div>
      </div>
    );
  }

  if (quizCompleted) {
    return (
      <div className="flex flex-col items-center justify-center min-h-screen">
        <div className="p-6 border rounded-lg shadow-md max-w-md text-center">
          <h2 className="mb-4 text-5xl font-bold text-green-600">Well Done!</h2>
          <p className="text-xl">You have completed the quiz successfully. Great job!</p>
          <ResultComponent resultData={resultData} /> 
        </div>
      </div>
    );
  }

  return (
    <div className="grid grid-rows-[20px_1fr_20px] items-center justify-items-center min-h-screen p-8 pb-20 gap-16 sm:p-20">
      <main className="flex flex-col gap-8 row-start-2 items-center sm:items-start">
        <p className="text-center mb-4 text-5xl font-bold">ClosedTrivia.</p>
        <QuizComponent quizData={quizData} onQuizComplete={handleQuizCompletion} /> 
      </main>
    </div>
  );
};

export default Home;
