public class Nation {
  Hashtable trait = new Hashtable();
  int uniqueID;
  private int xPos;
  private int yPos;
  String nameNation;
  int[] colorBaseArray = new int[3]; // need base colors for each nation
  int[][] colorBaseArraySquares = new int[numNationCharacteristics][3];
  int[][] traitPosArray = new int[numNationCharacteristics][2]; // save x,y for all characteristics
  
  Nation(int _uniqueID, String _nameNation, int _xPos, int _yPos, int _security, int _innovation, int _jobOpportunity, int _immigrationPolicy) {
    trait.put("security", _security);
    trait.put("innovation", _innovation);
    trait.put("jobOpportunity", _jobOpportunity);
    trait.put("immigrationPolicy", _immigrationPolicy);
    uniqueID = _uniqueID;
    nameNation = _nameNation;
    xPos = _xPos;
    yPos = _yPos;
    
    // each Nation object has unique color base
    for (int j=0;j<3;j++) {
      colorBaseArray[j] = int(random(255));
    }
    
    colorMode(HSB);
    for (int j=0;j<numNationCharacteristics;j++) {
      colorBaseArraySquares[j][0] = abs(colorBaseArray[0]+(int)random(-70,70)); 
      colorBaseArraySquares[j][1] = abs(colorBaseArray[1]+(int)random(-30,30)); 
      colorBaseArraySquares[j][2] = abs(colorBaseArray[1]+(int)random(-50,50)); 
    }
    colorMode(RGB);

    // generates random x,y standard for all the characteristics
    for (int i=0;i<numNationCharacteristics;i++) {
      traitPosArray[i][0] = xPos+int(random(-150, 150));
      traitPosArray[i][1] = yPos+int(random(-150, 150));
    }
  }
  
  void display(int setOpacity) {
    stroke(0);
    int i = 0;

    // loads rect diameters from hashtable, iterates thru all characteristics
    for (Enumeration e = trait.keys() ; e.hasMoreElements() ;) {
      fill(colorBaseArray[0]-i*30, colorBaseArray[1]-i*20, colorBaseArray[2], setOpacity);
      Object characteristic = e.nextElement();
      Object diameterRect = trait.get(characteristic);
      float _diameter = Float.parseFloat(diameterRect.toString());
      int diam = ceil(_diameter)*4;
      noStroke();
      rect(traitPosArray[i][0], traitPosArray[i][1], diam, diam);
      i++;
    }

    // displays main rect for each Nation
    fill(colorBaseArray[0],colorBaseArray[1],colorBaseArray[2]);
    rectMode(CENTER);
    rect(xPos,yPos,nationBoxSize,nationBoxSize);
    rectMode(CORNER);
    fill(255,255,0,200);
    textSize(globalLabelS);
    text(nameNation, xPos+15, yPos+15);
  } // display for Nation
  
  public int getXPos() {
    return xPos;
  }
  
  public int getYPos() {
    return yPos;
  }
  
}
