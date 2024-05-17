import React from 'react';
import styled, { keyframes } from 'styled-components';

const skeletonAnimation = keyframes`
  0% {
    background-color: rgba(165, 165, 165, 0.1);
  }
  50% {
    background-color: rgba(165, 165, 165, 0.3);
  }
  100% {
    background-color: rgba(165, 165, 165, 0.1);
  }
`;

const SkeletonWrapper = styled.div`
  width: 100%;
  height: 70vh;
  background-color: rgba(165, 165, 165, 0.1);
  border-radius: 4px;
  animation: ${skeletonAnimation} 1.5s infinite ease-in-out;
`;

const Skeleton = () => {
    return <SkeletonWrapper />;
};

export default Skeleton;