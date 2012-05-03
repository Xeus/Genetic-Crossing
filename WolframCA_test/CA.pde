// Wolfram Cellular Automata
// Daniel Shiffman <http://www.shiffman.net>

// A class to manage the CA

class CA {

  int generation;  // How many generations?
  int[] ruleset;   // An array to store the ruleset, for example {0,1,1,0,1,1,0,1}
  int w = 5;
  int[][] matrix;  // Store a history of generations in 2D array, not just one

  int cols, rows, datum;


  CA(int[] r) {
    ruleset = r;
    cols = width/w;
    rows = height/w;
    matrix = new int[cols][rows];
    restart();
  }
  
  // Reset to generation 0
  void restart() {
    for (int i = 0; i < cols; i++) {
      for (int j = 0; j < rows; j++) {
        matrix[i][j] = 0;
      }
    }
    //matrix[cols/2][0] = 1;    // We arbitrarily start with just the middle cell having a state of "1"
    //int[] traits = { 10, 8, 5, 6, 2, 4, 4, 1, 4, 8, 5, 8, 9, 6, 7, 2, 10, 1, 6, 6, 8, 3, 5 };
    //int[] traits = { 10, 2, 9, 2, 10, 1, 3, 8, 4, 6, 1, 7, 5, 8, 8, 8, 4, 2, 10, 9, 6, 6, 2 };
    
    for (int i=0; i<traits.size(); i++) {
      if (traits.get(i) < 4) {
        datum = 0;
      }
      else if (traits.get(i) < 8) {
        datum = 1;
      }
      else {
        datum = 2;
      }
      matrix[i][0] = datum;
    }
    
    generation = 0;
  }


  // The process of creating the new generation
  void generate() {

    // For every spot, determine new state by examing current state, and neighbor states
    // Ignore edges that only have one neighor
    for (int i = 0; i < cols; i++) {
      int left  = matrix[(i+cols-1)%cols][generation%rows];   // Left neighbor state
      int me    = matrix[i][generation%rows];       // Current state
      int right = matrix[(i+1)%cols][generation%rows];  // Right neighbor state
      matrix[i][(generation+1)%rows] = rules(left, me, right); // Compute next generation state based on ruleset
    }
    generation++;
  }

  // This is the easy part, just draw the cells, fill 255 for '1', fill 0 for '0'
  void display() {
    int offset = generation%rows;

    for (int i = 0; i < cols; i++) {
      for (int j = 0; j < rows; j++) {
        int y = j - offset;
        if (y <= 0) y = rows + y;
        if (matrix[i][j] == 1) {
          fill(0);
        }
        else if (matrix[i][j] == 2) {
          fill(0,255,255); 
        }
        else {
          fill(255);
        }
        noStroke();
        rect(i*w, (y-1)*w, w, w);
      }
    }
  }

  // Implementing the Wolfram rules
  // Could be improved and made more concise, but here we can explicitly see what is going on for each case
  int rules (int a, int b, int c) {
    String s = "" + a + b + c;
    int index = Integer.parseInt(s, 3);
    return ruleset[index];
  }

  // The CA is done if it reaches the bottom of the screen
  boolean finished() {
    if (generation > height/w) {
      return true;
    } 
    else {
      return false;
    }
  }
}

