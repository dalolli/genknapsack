package ga.ssGA;

public class ProblemKnapsack extends Problem {
  public double[] profits;
  public double[][] weights;
  public double[] limits;
  public int nRestrictions;

  public ProblemKnapsack(double[] profits, double[][] weights, double[] limits) {
    this.profits = profits;
    this.weights = weights;
    this.limits = limits;
    this.nRestrictions = limits.length;   // To save time later
  }

  public double Evaluate(Individual Indiv) {
    return KNAPSACK(Indiv);
  }


  //    PRIVATE METHODS

  // Count the number of 1's in the string
  private double KNAPSACK(Individual indiv)
  {
    double f = 0.0;
    double[] currentWeights = new double[nRestrictions]; // Vector of accumulated weights for each restriction

    for (int i = 0; i < CL; i++) {
      if (indiv.get_allele(i) == 1) {
        f += profits[i];  // We assume at first that the solution will be feasible
        for (int j = 0; j < nRestrictions; j++) {
          currentWeights[j] += weights[j][i];
        }
      }
    }

    for (int j = 0; j < nRestrictions; j++) {
      if (currentWeights[j] > limits[j]) {  // We check if any of the accumulated weights is higher than the allowed limit for the given restriction
        f = 0.0;   // The solution is not feasible
        break;
      }
    }

    indiv.set_fitness(f);
    return f;
  }

}