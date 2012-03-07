public class Religion {
  Hashtable trait = new Hashtable();
  int uniqueID, commercial, morality, hierarchy,portability;
  private int xPos;
  private int yPos;
  String nameReligion;
  int[] colorBaseArray = new int[3]; // need base colors for each religion
  int[][] colorBaseArraySquares = new int[numReligionCharacteristics][3];
  int[][] traitPosArray = new int[numReligionCharacteristics][2]; // save x,y for all characteristics
  
  Religion(int _uniqueID, String _nameReligion, int _xPos, int _yPos, int _commercial, int _morality, int _hierarchy, int _portability) {
    trait.put("commercial", _commercial);
    trait.put("morality", _morality);
    trait.put("hierarchy", _hierarchy);
    trait.put("portability", _portability);
    uniqueID = _uniqueID;
    nameReligion = _nameReligion;
    commercial = _commercial;
    morality = _morality;
    hierarchy = _hierarchy;
    portability = _portability;
    xPos = _xPos;
    yPos = _yPos;
    
    // each religion object has unique color base
    for (int j=0;j<3;j++) {
      colorBaseArray[j] = int(random(255));
    }
    
    colorMode(HSB);
    for (int j=0;j<numReligionCharacteristics;j++) {
      colorBaseArraySquares[j][0] = abs(colorBaseArray[0]+(int)random(-70,70)); 
      colorBaseArraySquares[j][1] = abs(colorBaseArray[1]+(int)random(-30,30)); 
      colorBaseArraySquares[j][2] = abs(colorBaseArray[1]+(int)random(-50,50)); 
    }
    colorMode(RGB);

    // generates random x,y standard for all the characteristics
    for (int i=0;i<numReligionCharacteristics;i++) {
      traitPosArray[i][0] = xPos+int(random(-150, 150));
      traitPosArray[i][1] = yPos+int(random(-150, 150));
    }
  }
  
  void display(int setOpacity) {
    stroke(0);
    int i = 0;

    // loads ellipse diameters from hashtable, iterates thru all characteristics
    for (Enumeration e = trait.keys() ; e.hasMoreElements() ;) {
      fill(colorBaseArray[0]-i*30, colorBaseArray[1]-i*20, colorBaseArray[2], setOpacity);
      Object characteristic = e.nextElement();
      Object diameterEllipse = trait.get(characteristic);
      float _diameter = Float.parseFloat(diameterEllipse.toString());
      int diam = ceil(_diameter)*4;
      noStroke();
      ellipse(traitPosArray[i][0], traitPosArray[i][1], diam, int(diam*0.66));
      i++;
    }

    // displays main ellipse for each religion
    fill(colorBaseArray[0],colorBaseArray[1],colorBaseArray[2]);
    ellipse(xPos,yPos,religionBoxSize,int(religionBoxSize*0.66));
    fill(3, 255, 181,200);
    textSize(globalLabelS);
    text(nameReligion, xPos+15, yPos+15);
  } // display for religion
  
  public int getXPos() {
    return xPos;
  }
  
  public int getYPos() {
    return yPos;
  }
  
}
