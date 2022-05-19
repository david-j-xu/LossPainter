# LossPainter

## Overview
I wanted to create a program which, given a source image, can nondeterministically create a painting by digitally placing paint brush strokes on a canvas.

Clearly, the search space is combinatorial and the problem is NP-hard even under simplifying assumptions. However, the problem does enjoy some nice structure, so it seemed like a problem that can be aided with some optimization techniques. The objective we hope to minimize will be some distance between our painting and the source image. I chose to use the L2 norm between the colors on each pixel. To decrease the euclidean distance then, it is well known that we can place a brush stroke with the mean color at that location.

However, the problem still remains that searching for the locally optimal minimizing brush stroke is still computationally expensive (though polynomial), and infeasible for my laptop to run. Therefore, I opted for a simpler approach, using essentially random walks. I used a randomly perturbed grid of points at each step to select candidate points, each of which would be painted as described above. This in expectation will decrease the value of the loss function as per the optimality of the mean with respect to the L2-norm. This process is continued until the loss function decreases by an insignificant amount (<1% decrease). This process would be repeated multiple times with ever smaller brush sizes, until the desired result was reached.

This stochastic approach, while crude, actually manages some decent results, while keeping computation cost relatively low for smallish (1000 x 1000) images.

## Examples
Here is an example of a result created with this program, depicting two parrots.
![parrots](https://user-images.githubusercontent.com/63666552/169396776-6f848445-c9bf-4b7b-9d43-106e34e50dbb.png)
