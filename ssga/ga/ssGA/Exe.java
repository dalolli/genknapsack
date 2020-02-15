///////////////////////////////////////////////////////////////////////////////
///            Steady State Genetic Algorithm v1.0                          ///
///                by Enrique Alba, July 2000                               ///
///                                                                         ///
///   Executable: set parameters, problem, and execution details here       ///
///////////////////////////////////////////////////////////////////////////////

package ga.ssGA;

import java.io.File;
import java.util.Scanner;

public class Exe
{

  public static int NEXECUTIONS = 100;

  public static void main(String args[]) throws Exception
  {
    /*     
    // PARAMETERS PPEAKS 
    int    gn         = 512;                                    // Gene number
    int    gl         = 1;                                      // Gene length
    int    popsize    = 512;                                    // Population size
    double pc         = 0.8;                                    // Crossover probability
    double pm  = 1.0/(double)((double)gn*(double)gl);           // Mutation probability
    double tf         = (double)1 ;                             // Target fitness beign sought
    long   MAX_ISTEPS = 50000;
    */
 
    /*     
    // PARAMETERS ONEMAX
    int    gn         = 512;                                    // Gene number
    int    gl         = 1;                                      // Gene length
    int    popsize    = 512;                                    // Population size
    double pc         = 0.8;                                    // Crossover probability
    double pm  = 1.0/(double)((double)gn*(double)gl);           // Mutation probability
    double tf         = (double)gn*gl ;                         // Target fitness being sought
    long   MAX_ISTEPS = 50000;
    */  

    File file = new File("./ga/ssGA/mknap1.txt");
    Scanner sc = new Scanner(file);

    int nProblems = sc.nextInt();                               // Number of test problems

    for (int p = 1; p <= nProblems; p++) {

      System.out.println("Problem " + p + ":");

      int nItems = sc.nextInt();                                // Number of items
      int nConstraints = sc.nextInt();                          // Number of constraints
      double targetFitness = sc.nextDouble();                   // Target fitness being sought

      sc.nextLine();
      
      double[] profits = new double[nItems];                    // Vector of profits for each item
      String s = sc.nextLine();
      String[] aux = s.trim().split(" ");
      for (int i = 0; i < nItems; i++) {
        profits[i] = Double.parseDouble(aux[i]);
      }

      double[][] weights = new double[nConstraints][nItems];    // Vector of weights for each constraint and item     
      for (int i = 0; i < nConstraints; i++) {
        s = sc.nextLine();
        aux = s.trim().split(" ");
        for (int j = 0; j < nItems; j++) {
          weights[i][j] = Double.parseDouble(aux[j]);
        }
      }

      double[] limits = new double[nConstraints];               // Vector of resource limits for each constraint
      s = sc.nextLine();
      aux = s.trim().split(" ");
      for (int i = 0; i < nConstraints; i++) {
        limits[i] = Double.parseDouble(aux[i]);
      }

      // PARAMETERS KNAPSACK
      int    gn         = nItems;                               // Gene number
      int    gl         = 1;                                    // Gene length
      int    popsize    = (p != 6) ? 1024 : 512;                // Population size (default is 512)
      double pc         = 1.0;                                  // Crossover probability (default is 0.8)
      double pm         = (p != 7) ? 0.07 : 0.05;               // Mutation probability (default is 1/gn)
      double tf         = targetFitness;                        // Target fitness being sought
      long   MAX_ISTEPS = 100000;                               // Max number of steps (default is 50000)

      Problem   problem;                                        // The problem being solved

      double bestTF, averageTF, hitrate;
      bestTF = averageTF = hitrate = 0;
      double worstTF = Double.MAX_VALUE;
      int averageSteps = 0;

      for (int exec = 1; exec <= NEXECUTIONS; exec++) {
        // problem = new ProblemPPeaks(); 
        // problem = new ProblemOneMax();
        problem = new ProblemKnapsack(profits, weights, limits);

        problem.set_geneN(gn);
        problem.set_geneL(gl);
        problem.set_target_fitness(tf);

        Algorithm ga;                                           // The ssGA being used
        ga = new Algorithm(problem, popsize, gn, gl, pc, pm);

        for (int step=0; step<MAX_ISTEPS; step++) {  
          ga.go_one_step();
          averageSteps++;
          // System.out.printf("%d  %.2f%n", step, ga.get_avgf());

          if((problem.tf_known()) && (ga.get_solution()).get_fitness()>=problem.get_target_fitness()) {
            // System.out.println("Solution Found! After " + problem.get_fitness_counter() + " evaluations");
            break;
          }
        }

        // Print the solution
        /*
        for(int i=0;i<gn*gl;i++)
          System.out.println((ga.get_solution()).get_allele(i));
        */

        double currentSolution = (ga.get_solution()).get_fitness();

        bestTF = (currentSolution > bestTF) ? currentSolution : bestTF;
        worstTF = (currentSolution < worstTF) ? currentSolution : worstTF;
        averageTF += currentSolution;
        hitrate = (currentSolution >= problem.get_target_fitness()) ? hitrate + 1 : hitrate;
      }

      averageTF = (double) averageTF / NEXECUTIONS;
      hitrate =  (double) hitrate / NEXECUTIONS;
      averageSteps /= NEXECUTIONS;

      System.out.printf("%n%.2f %.2f %.2f %.2f %d%n%n", hitrate * 100, worstTF, bestTF, averageTF, averageSteps);      
    }
  }
}
// END OF CLASS: Exe
