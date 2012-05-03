public class Nation extends VerletParticle2D {
  Hashtable trait = new Hashtable();
  int uniqueID, security, innovation, jobOpportunity, immigrationPolicy,lifeExpectancy,
  education, sanitation, standardOfLiving,pollution,biodiversity,crime,politicalFreedom,nutrition;
  String nameNation;
  int[] colorBaseArray = new int[3]; // need base colors for each nation
  int[][] colorBaseArraySquares = new int[numNationCharacteristics][3];
  float[][] traitPosArray = new float[numNationCharacteristics][2]; // save x,y for all characteristics
  
  Nation(int _uniqueID, String _nameNation, float x, float y, int _security, int _innovation, int _jobOpportunity, int _immigrationPolicy,
  int _lifeExpectancy, int _education, int _sanitation, int _standardOfLiving, int _pollution, int _biodiversity, int _crime, int _politicalFreedom, int _nutrition) {
    super(x, y);
    trait.put("security", _security);
    trait.put("innovation", _innovation);
    trait.put("jobOpportunity", _jobOpportunity);
    trait.put("immigrationPolicy", _immigrationPolicy);
    trait.put("lifeExpectancy", _lifeExpectancy);
    trait.put("education", _education);
    trait.put("sanitation", _sanitation);
    trait.put("standardOfLiving", _standardOfLiving);
    trait.put("pollution", _pollution);
    trait.put("biodiversity", _biodiversity);
    trait.put("crime", _crime);
    trait.put("politicalFreedom", _politicalFreedom);
    trait.put("nutrition", _nutrition);
    uniqueID = _uniqueID;
    nameNation = _nameNation;
    security = _security;
    innovation = _innovation;
    jobOpportunity = _jobOpportunity;
    immigrationPolicy = _immigrationPolicy;
    lifeExpectancy = _lifeExpectancy;
    education = _education;
    sanitation = _sanitation;
    standardOfLiving = _standardOfLiving;
    pollution = _pollution;
    biodiversity = _biodiversity;
    crime = _crime;
    politicalFreedom = _politicalFreedom;
    nutrition = _nutrition;
    
    // each Nation object has unique color base
    for (int j=0;j<3;j++) {
      colorBaseArray[j] = int(random(255));
    }
    
    colorMode(HSB);
    for (int j=0;j<numNationCharacteristics;j++) {
      colorBaseArraySquares[j][0] = 50; //abs(colorBaseArray[0]+(int)random(-70,70)); 
      colorBaseArraySquares[j][1] = 50; //abs(colorBaseArray[1]+(int)random(-30,30)); 
      colorBaseArraySquares[j][2] = 50; //abs(colorBaseArray[1]+(int)random(-50,50)); 
    }
    colorMode(RGB);

    // generates random x,y standard for all the characteristics
    for (int i=0;i<numNationCharacteristics;i++) {
      traitPosArray[i][0] = x+random(-150, 150);
      traitPosArray[i][1] = y+random(-150, 150);
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
    rect(x,y,nationBoxSize,nationBoxSize);
    rectMode(CORNER);
    fill(255,255,0,200);
    textSize(globalLabelS);
    text(nameNation, x+15, y+15);
  } // display for Nation
  
  public float getXPos() {
    return x;
  }
  
  public float getYPos() {
    return y;
  }
  
}
