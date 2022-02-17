// Pablo Mateos García
package bluecow;
import java.io.*;  
import java.util.ArrayList;
import java.util.Collections;

public class BlueCow {
    static final int MAX = 50; //Maximum number of cities
    static final int MIN = 30; //Minimum number of cities
    static final int MIN_DISTANCE = 60000; //Minimun distance of the tour
    static final int MAX_DISTANCE = 97500; //Maximum  distance of the tour
    static final int MIN_CITIES = 100; //Minimum distance between cities
    static final int MAX_CITIES = 2000; //Maximum distance between cities
    static final int MIN_CONTINENT = 4; //Minimum number of cities per continent
    static final int MAX_CONTINENT = 9; //Maximum number of cities per continent
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        File capitalsCSV = new File("capitals.csv");
        FileReader fr = new FileReader(capitalsCSV);  
        BufferedReader br = new BufferedReader(fr);
        ArrayList <Capital> capitals = new ArrayList<>(); //ArrayList that includes ALL the capitals
        
        //The file is being read and the money is being generated randomly, depending on the continent
        String lineCSV = br.readLine();
        if(lineCSV != null){
            do{
                String [] CSVdata = lineCSV.split(",");
                Capital newCapital = new Capital();
                newCapital.setCountryName(CSVdata[0]);
                newCapital.setName(CSVdata[1]);
                newCapital.setLatitude(Double.parseDouble(CSVdata[2]));
                newCapital.setLongitude(Double.parseDouble(CSVdata[3]));
                newCapital.setContinent(CSVdata[5]);
                newCapital.setChosen(false);
                double money = 0;
                switch(CSVdata[5]){
                    case "Europe":
                        money = (Math.random()*((10000-5000)+1))+5000;
                        break;
                    case "Africa":
                        money = (Math.random()*((3000-1000)+1))+3000;
                        break;
                    case "North America":
                        money = (Math.random()*((10000-5000)+1))+5000;
                        break;
                    case "Central America":
                        money = (Math.random()*((7000-3500)+1))+3500;
                        break;
                    case "South America":
                        money = (Math.random()*((7000-3500)+1))+3500;
                        break;
                    case "Asia":
                        money = (Math.random()*((8000-4000)+1))+4000;
                        break;
                    default:
                        money = (Math.random()*((7000-3500)+1))+3500;
                        break;
                }
                newCapital.setMoney(money);
                capitals.add(newCapital);
                lineCSV = br.readLine();
            }while(lineCSV != null);
        }
        
        /*
        Total number of cities: 245
        Europe: 58
        Africa: 58
        Asia: 46
        North America: 25
        Central America & South America: 26
        Australia & Antarctica: 32
        
        IT CAN BE BETWEEN 4 AND 9 CITIES PER EACH CONTINENT, THAT MEANS, AS MINIMUM THERE WILL BE 24 CITIES AND AS MAXIMUM 54 CITIES
        MINIMUM AND MAXIMUM OF THE TOUR: 30-50
        */
        
        //We add to bestCapitals the maximum number of cities per continent permitted. They will be sorted by the money.
        Collections.sort(capitals, new ComparatorByMoney());
        int i, eu =0, as=0, af=0, na=0, sa=0, au=0;
        ArrayList<Capital> bestCapitals = new ArrayList<>(MAX);
        ArrayList<Capital> finalRoute = new ArrayList<>(MAX);
        while(eu < MAX_CONTINENT && as < MAX_CONTINENT && af < MAX_CONTINENT && na < MAX_CONTINENT && sa < MAX_CONTINENT && au < MAX_CONTINENT){
            for(Capital c:capitals){
                switch(c.getContinent()){
                        case "Europe":
                            if(eu<MAX_CONTINENT){
                                bestCapitals.add(c);
                                eu++;
                            }
                            break;
                        case "Africa":
                            if(af<MAX_CONTINENT){
                                bestCapitals.add(c);
                                af++;
                            }
                            break;
                        case "North America":
                            if(na<MAX_CONTINENT){
                                bestCapitals.add(c);
                                na++;
                            }
                            break;
                        case "Central America":
                            if(sa<MAX_CONTINENT){
                                bestCapitals.add(c);
                                sa++;
                            }
                            break;
                        case "South America":
                            if(sa<MAX_CONTINENT){
                                bestCapitals.add(c);
                                sa++;
                            }
                            break;
                        case "Asia":
                            if(as<MAX_CONTINENT){
                                bestCapitals.add(c);
                                as++;
                            }
                            break;
                        default:
                            if(au<MAX_CONTINENT){
                                bestCapitals.add(c);
                                au++;
                            }
                            break;
                    }
                }
        }
        
        //We delete the cities we don't need to fit the maximum constraint, having in mind that we need to consider the minimum constraint per continent too
        while(bestCapitals.size()>MAX){
            if(numCities(bestCapitals,bestCapitals.get(bestCapitals.size()-1).getContinent())>MIN_CONTINENT){
                bestCapitals.remove(bestCapitals.size()-1);
            }
        }        
        
        //We change to true if the city has been chosen
        for(Capital capital:bestCapitals){
            for(Capital capital2:capitals){
                if(capital.equals(capital2)){
                    capital.setChosen(true);
                    capital2.setChosen(true);
                }
            }
        }
        
        //We will save in citiesD the distances between the selected cities later
        double[][] citiesD;
        citiesD = new double[MAX][MAX];
        
        //We launch the MAIN LOOP
        bestRoute(citiesD, bestCapitals, capitals, finalRoute);
            
    
    }
    
    static void bestRoute(double[][] citiesD, ArrayList<Capital> bestCapitals, ArrayList<Capital> capitals, 
            ArrayList<Capital> finalRoute){
        
        int city=0, newCity=0, k, i, j;
        double lessDistance, sum = 0;
        
        finalRoute.clear(); //Final route will be empty at the beginning of the loop
        citiesD = SetMatrixDistances(bestCapitals); //We set the values of the matrix of distances
        Collections.sort(bestCapitals, new ComparatorByMoney());//We sort the cities taking into account the money earned
        finalRoute.add(bestCapitals.get(0)); //We start in the first city of the array (the city that gives more money
        
        System.out.println("");
        System.out.println("Optimizing route...");
        
        
        
        for (k=1;k<MAX;k++){
            lessDistance = 100000000;
            city = newCity;
            
            for (i = 0; i < bestCapitals.size(); i++){
                if (citiesD[city][i] < lessDistance && city != i && finalRoute.contains(bestCapitals.get(i))==false){ 
                    lessDistance = citiesD[city][i]; //We are going to go to the closest city from the city we are now
                    newCity = i;
                    
                    if(lessDistance<MIN_CITIES || lessDistance>MAX_CITIES){ //If the closest city is too close/too far
                        for (Capital capital : capitals){
                            
                            if( capital.getContinent().equals( bestCapitals.get(newCity).getContinent() ) && capital.isChosen() == false){ //We change it to the next one not chosen of the same continent (to avoid having more or less per continent)
                                bestCapitals.remove(newCity);
                                capital.setChosen(true);
                                bestCapitals.add(capital);
                                bestRoute(citiesD, bestCapitals, capitals, finalRoute);
                                break;
                            }
                        }
                    }   
                }
            }  
            
            sum = lessDistance + sum;
            finalRoute.add(bestCapitals.get(newCity)); //if everything is okay, we add the city to the finalRoute array,
        }
        
        double distanceBeginEnd = CalculateDistances(finalRoute.get(0),finalRoute.get(finalRoute.size()-1)); //We calculate we distance between the last one and the first one, as it is a circuit
        if(distanceBeginEnd>MIN_CITIES || distanceBeginEnd<MAX_CITIES){ //If it is a valid distance
            sum = sum + distanceBeginEnd;

            if(sum<=MAX_DISTANCE && sum>=MIN_DISTANCE){ //If the total distance is valid, we have a valid route
                System.out.println("");
                System.out.println("VALID ROUTE FOUND.");
                System.out.println("");
                
                double sumaPrueba = 0;
                int cityCounter = 1;
                
                for (Capital capital : finalRoute){
                    System.out.println("City " + cityCounter + ": " + capital.getName() + ", " + capital.getContinent());
                    sumaPrueba = sumaPrueba + capital.getMoney();
                    cityCounter++;
                }
                
                System.out.println("");
                System.out.println("TOTAL AMOUNT OF MONEY: " + sumaPrueba);
                System.out.println("");
                System.out.println("The tour will have the following number of cities per continent: ");
                System.out.println("Europe: " + numCities(bestCapitals, "Europe"));
                System.out.println("North America: " + numCities(bestCapitals, "North America"));
                System.out.println("Central & South America: " + (numCities(bestCapitals, "Central America")+numCities(bestCapitals, "South America")));
                System.out.println("Asia: " + numCities(bestCapitals, "Asia"));
                System.out.println("Africa: " + numCities(bestCapitals, "Africa"));
                System.out.println("Antarctica & Australia: " + (numCities(bestCapitals, "Antarctica")+numCities(bestCapitals, "Australia")));
            }
            else{ //If the total distance is not valid, we change the cities one by one from the bottom of the list (lest money) and try again to see if it is a valid route
                int prueba = 0;
                for(i=bestCapitals.size()-1;i>=0;i--){
                    for (Capital capital : capitals){
                            if(capital.getContinent().equals( bestCapitals.get(i).getContinent() ) && capital.isChosen() == false){
                                bestCapitals.remove(i);
                                capital.setChosen(true);
                                bestCapitals.add(capital);
                                bestRoute(citiesD, bestCapitals, capitals, finalRoute/*, s, a*/);
                                break;
                            }
                    }
                    
                }
                //If we changed every single  city and we cannot find a good route, we are going to remove cities until we reach the minimum allowed and taking into account the maximum/minimum per continent
                for(Capital c: bestCapitals){ 
                    if(bestCapitals.size()>MIN){
                        if(numCities(bestCapitals,c.getContinent())>4){
                            bestCapitals.remove(c);
                            bestRoute(citiesD, bestCapitals, capitals, finalRoute/*, s, a*/);
                        }
                    }
                }
                //If we reach the minimum (30) and we could not find a proper route while doing it, we will generate a random route that suits the constraints
                System.out.println("");
                System.out.println("Optimizing the route has been tried. As it has been impossible to find an appropiate route, the application will generate a random solution.");
           
                do{
                    System.out.println("");
                    System.out.println("Generating random route that fits the constraints...");
                    Collections.shuffle(capitals);
                    bestCapitals.clear();
                    for(Capital c: capitals){
                        c.setChosen(false);
                    }
                    for(i=0;i<MAX;i++){
                        capitals.get(i).setChosen(true);
                        bestCapitals.add(capitals.get(i));
                    }
                }while(minMaxBestCapitals(bestCapitals)!=true);
                bestRoute(citiesD, bestCapitals, capitals, finalRoute);
            }
            System.exit(0);
        }
        else{ //If the distance from the last city to the first one is not valid, we change the last city for one of the same continent and taking into account the monet
            for (Capital capital : capitals){
                if( capital.getContinent().equals(finalRoute.get(finalRoute.size()-1).getContinent() ) && capital.isChosen() == false){
                    bestCapitals.remove(finalRoute.get(finalRoute.size()-1));
                    capital.setChosen(true);
                    bestCapitals.add(capital);
                    bestRoute(citiesD, bestCapitals, capitals, finalRoute/*, s, a*/);
                    break;
                }
            }
        }
    }
    
    
    public static double Radians(double x){ //Function that converts the latitude and longitude to radians
        return (Math.PI / 180) * x;
    }
    
    static int numCities(ArrayList <Capital> arrayCities, String continent){ //Function that returns the number of cities of a specific continent in an arraylist of cities
        int cont = 0;
        for(Capital capital: arrayCities){
            if(capital.getContinent().compareToIgnoreCase(continent)==0){
                cont++;
            }
        }
        return cont;
    }
    
    static boolean minMaxBestCapitals(ArrayList <Capital> bestCapitals){ //Function that tells you if the cities of the array are following the maximum/minimum constraints
        if(numCities(bestCapitals, "Europe")>=MIN_CONTINENT && numCities(bestCapitals, "Europe")<=MAX_CONTINENT){
            if(numCities(bestCapitals, "Asia")>=MIN_CONTINENT && numCities(bestCapitals, "Asia")<=MAX_CONTINENT){
                if(numCities(bestCapitals, "Africa")>=MIN_CONTINENT && numCities(bestCapitals, "Africa")<=MAX_CONTINENT){
                    if(numCities(bestCapitals, "North America")>=MIN_CONTINENT && numCities(bestCapitals, "North America")<=MAX_CONTINENT){
                        if((numCities(bestCapitals, "Central America")+numCities(bestCapitals, "South America"))>=MIN_CONTINENT && (numCities(bestCapitals, "Central America")+numCities(bestCapitals, "South America"))<=MAX_CONTINENT){
                            if((numCities(bestCapitals, "Australia")+numCities(bestCapitals, "Antarctica"))>=MIN_CONTINENT && (numCities(bestCapitals, "Australia")+numCities(bestCapitals, "Antarctica"))<=MAX_CONTINENT){
                                return true;
                            }
                            else{
                                return false;
                            }
                        }
                        else{
                            return false;
                        }
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }
    
    static double CalculateDistances(Capital capital1, Capital capital2){ //Function that calculates the distances between two cities
        double earthRadius = 6378.0;
        
        double posLatitude1Rad =  Radians(capital1.getLatitude());
        double posLatitude2Rad = Radians(capital2.getLatitude());
        double posLongitude1Rad = Radians(capital1.getLongitude());
        double posLongitude2Rad = Radians(capital2.getLongitude());
        
        double difLatitude =  Math.abs(posLatitude1Rad - posLatitude2Rad);
        double difLongitude = Math.abs(posLongitude1Rad - posLongitude2Rad);
        
        double a = Math.pow( Math.sin(difLatitude/2), 2 ) + Math.cos(posLatitude1Rad) * Math.cos(posLatitude2Rad) * Math.pow( Math.sin(difLongitude/2), 2);
        
        double c = 2 * Math.atan2( Math.sqrt(a), Math.sqrt(1-a) );
        
        return earthRadius * c;
    }
    
    static double[][] SetMatrixDistances(ArrayList<Capital> bestCapitals){ //Function that sets the matrix of distances
        
        double[][] cities = new double[MAX][MAX];
        double distance;
        int i = 0,j;
        
        for ( i = 0; i<bestCapitals.size(); i++) {
            
            for ( j = 0; j < bestCapitals.size(); j++) {
                
                distance = CalculateDistances(bestCapitals.get(i),bestCapitals.get(j));
                
                cities[i][j] = distance;
            }
        }
        
        return cities;
        
    }

}
    
    /*CODIGO COMPLETO SIN LIMPIAR

    
    package bluecow;
    import java.io.*;  
    import java.util.ArrayList;
    import java.util.Collections;

    public class BlueCow {
        static final int MAX = 50; //Maximum number of cities
        static final int MIN = 30; //Minimum number of cities
        static final int MIN_DISTANCE = 60000; //Minimun distance of the tour
        static final int MAX_DISTANCE = 100000; //Maximum  distance of the tour
        static final int MIN_CITIES = 100; //Minimum distance between cities
        static final int MAX_CITIES = 2000; //Maximum distance between cities
        static final int MIN_CONTINENT = 4; //Minimum number of cities per continent
        static final int MAX_CONTINENT = 9; //Maximum number of cities per continent

        public static void main(String[] args) throws FileNotFoundException, IOException {

            File capitalsCSV = new File("capitals.csv");
            FileReader fr = new FileReader(capitalsCSV);  
            BufferedReader br = new BufferedReader(fr);
            ArrayList <Capital> capitals = new ArrayList<>(); //ArrayList that includes ALL the capitals

            //The file is being read and the money is being generated randomly, depending on the continent
            String lineCSV = br.readLine();
            if(lineCSV != null){
                do{
                    String [] CSVdata = lineCSV.split(",");
                    Capital newCapital = new Capital();
                    newCapital.setCountryName(CSVdata[0]);
                    newCapital.setName(CSVdata[1]);
                    newCapital.setLatitude(Double.parseDouble(CSVdata[2]));
                    newCapital.setLongitude(Double.parseDouble(CSVdata[3]));
                    newCapital.setContinent(CSVdata[5]);
                    newCapital.setChosen(false);
                    double money = 0;
                    switch(CSVdata[5]){
                        case "Europe":
                            money = (Math.random()*((10000-5000)+1))+5000;
                            break;
                        case "Africa":
                            money = (Math.random()*((3000-1000)+1))+3000;
                            break;
                        case "North America":
                            money = (Math.random()*((10000-5000)+1))+5000;
                            break;
                        case "Central America":
                            money = (Math.random()*((7000-3500)+1))+3500;
                            break;
                        case "South America":
                            money = (Math.random()*((7000-3500)+1))+3500;
                            break;
                        case "Asia":
                            money = (Math.random()*((8000-4000)+1))+4000;
                            break;
                        default:
                            money = (Math.random()*((7000-3500)+1))+3500;
                            break;
                    }
                    newCapital.setMoney(money);
                    capitals.add(newCapital);
                    lineCSV = br.readLine();
                }while(lineCSV != null);
            }

            /*
            Total: 245
            Europa: 58
            Africa: 58
            Asia: 46
            America Norte: 25
            America Centro - Sur: 26
            Australia: 32

            ELEGIMOS ENTRE 4 Y 9 CIUDADES POR CADA CONTINENTE, ES DECIR, COMO MINIMO HABRA 24 Y MAXIMO 54
            RANGO DE CIUDADES: ENTRE 30 Y 50
            

            Collections.sort(capitals, new ComparatorByMoney());
            int i, eu =0, as=0, af=0, na=0, sa=0, au=0;
            ArrayList<Capital> arrayCities = new ArrayList<>();
            ArrayList<Capital> bestCapitals = new ArrayList<>(MAX);
            ArrayList<Capital> finalRoute = new ArrayList<>(MAX);
            while(eu < MAX_CONTINENT && as < MAX_CONTINENT && af < MAX_CONTINENT && na < MAX_CONTINENT && sa < MAX_CONTINENT && au < MAX_CONTINENT){
                for(Capital c:capitals){
                    switch(c.getContinent()){
                            case "Europe":
                                if(eu<MAX_CONTINENT){
                                    bestCapitals.add(c);
                                    eu++;
                                }
                                break;
                            case "Africa":
                                if(af<MAX_CONTINENT){
                                    bestCapitals.add(c);
                                    af++;
                                }
                                break;
                            case "North America":
                                if(na<MAX_CONTINENT){
                                    bestCapitals.add(c);
                                    na++;
                                }
                                break;
                            case "Central America":
                                if(sa<MAX_CONTINENT){
                                    bestCapitals.add(c);
                                    sa++;
                                }
                                break;
                            case "South America":
                                if(sa<MAX_CONTINENT){
                                    bestCapitals.add(c);
                                    sa++;
                                }
                                break;
                            case "Asia":
                                if(as<MAX_CONTINENT){
                                    bestCapitals.add(c);
                                    as++;
                                }
                                break;
                            default:
                                if(au<MAX_CONTINENT){
                                    bestCapitals.add(c);
                                    au++;
                                }
                                break;
                        }
                    }
            }/*
            eu =0;
            as=0;
            af=0; 
            na=0; 
            sa=0; 
            au=0;
            for(Capital c:bestCapitals){
                switch(c.getContinent()){
                        case "Europe":
                            eu++;
                            break;
                        case "Africa":
                            af++;
                            break;
                        case "North America":
                            na++;
                            break;
                        case "Central America":
                            sa++;
                            break;
                        case "South America":
                            sa++;
                            break;
                        case "Asia":
                            as++;
                            break;
                        default:
                            au++;
                            break;
                    }
            }
            System.out.println("EU: " + eu + " AF: " + af + "AS: " + as + "AU: " + au + "NA: " + na + "SA: " + sa);
            
            //Delete the cities that we don't need
            while(bestCapitals.size()>MAX){
                if(numCities(bestCapitals,bestCapitals.get(bestCapitals.size()-1).getContinent())>MIN_CONTINENT){
                    bestCapitals.remove(bestCapitals.size()-1);
                }
            }        
            eu =0;
            as=0;
            af=0; 
            na=0; 
            sa=0; 
            au=0;
            for(Capital c:bestCapitals){
                switch(c.getContinent()){
                        case "Europe":
                            eu++;
                            break;
                        case "Africa":
                            af++;
                            break;
                        case "North America":
                            na++;
                            break;
                        case "Central America":
                            sa++;
                            break;
                        case "South America":
                            sa++;
                            break;
                        case "Asia":
                            as++;
                            break;
                        default:
                            au++;
                            break;
                    }
            }
            System.out.println("EU: " + eu + " AF: " + af + "AS: " + as + "AU: " + au + "NA: " + na + "SA: " + sa);
            
            double[][] citiesD;
            citiesD = new double[MAX][MAX];


            //Change to true if the city has been chosen
            for(Capital capital:bestCapitals){
                for(Capital capital2:capitals){
                    if(capital.equals(capital2)){
                        capital.setChosen(true);
                        capital2.setChosen(true);
                    }
                }
            }
            
            for(int j = 0; j<MAX; j++){
                System.out.println( String.format("%s",bestCapitals.get(j).getName()) );
            }
            
            citiesD = SetMatrixDistances(bestCapitals);
            
            System.out.printf("\n\n");

            for(i = 0; i<MAX; i++){
                for(int j= 0; j<MAX; j++)
                {
                    System.out.printf(" %.2f ", citiesD[i][j]);
                }
                System.out.printf("\n");
            }
        

            //Cities conected 
            
            int [] s = new int[arrayCities.size()];


            s[0] = 1;

            for (i = 1; i < MAX; i++)
            {
                s[i] = 0;
            }

            for (i = 0; i < MAX; i++)
            {
                System.out.println(s[i]);
            }

            int j;

            int[][] a = new int[MAX][MAX];


            for (i = 0; i < MAX; i++)
            {
                for (j = 0; j < MAX; j++)
                {
                    a[i][j] = 0;
                }
            }
            
            bestRoute(citiesD, arrayCities, bestCapitals, capitals, finalRoute/*, s, a);


        }

        static void bestRoute(double[][] citiesD, ArrayList<Capital> arrayCities, ArrayList<Capital> bestCapitals, ArrayList<Capital> capitals, 
                ArrayList<Capital> finalRoute/*, int[] s, int[][] a){

            finalRoute.clear();
            int city=0, newCity=0, k, i;
            System.out.println("");
            System.out.println("Optimizing route...");
            //s = new int[arrayCities.size()];
            //System.out.println("HAY CIUDADES LAS SIGUIENTES: " + bestCapitals.size());
            /*int eu, as, af, na, sa, au;
            eu =0;
            as=0;
            af=0; 
            na=0; 
            sa=0; 
            au=0;
            for(Capital c:bestCapitals){
                switch(c.getContinent()){
                        case "Europe":
                            eu++;
                            break;
                        case "Africa":
                            af++;
                            break;
                        case "North America":
                            na++;
                            break;
                        case "Central America":
                            sa++;
                            break;
                        case "South America":
                            sa++;
                            break;
                        case "Asia":
                            as++;
                            break;
                        default:
                            au++;
                            break;
                    }
            }
            //System.out.println("EU: " + eu + " AF: " + af + "AS: " + as + "AU: " + au + "NA: " + na + "SA: " + sa);

            /*
            s[0] = 1;

            for (i = 1; i < arrayCities.size(); i++)
            {
                s[i] = 0;
            }

            for (i = 0; i < arrayCities.size(); i++)
            {
                System.out.println(s[i]);
            }



            a = new int[MAX][MAX];


            for (i = 0; i < arrayCities.size(); i++)
            {
                for (j = 0; j < arrayCities.size(); j++)
                {
                    a[i][j] = 0;
                }
            }
            
            int j;
            double lessDistance, sum = 0;
            //System.out.println("ENTRAMOS EN EL BUCLE y ordenamos");
            Collections.sort(bestCapitals, new ComparatorByMoney());
            finalRoute.add(bestCapitals.get(0));
            for (k=1;k<MAX;k++)
            {
                lessDistance = 100000000;
                //Console.WriteLine("k: " + k);
                /*for (j = 0; j < MAX; j++)
                {

                    //Console.WriteLine("j: " + j);
                    if (j == lastAdded) {
                        city = newCity;
                        for (i = 0; i < bestCapitals.size(); i++)
                        {
                            if (citiesD[city][i] < lessDistance && city != i /*&& s[i]==0 && finalRoute.contains(bestCapitals.get(i))==false)
                            {
                                lessDistance = citiesD[city][i];
                                newCity = i;
                                if(lessDistance<MIN_CITIES || lessDistance>MAX_CITIES){
                                    //System.out.println("DISTANCIA MAYOR MENOR MAL");
                                    for (Capital capital : capitals) {
                                        if( capital.getContinent().equals( bestCapitals.get(newCity).getContinent() ) && capital.isChosen() == false){
                                            bestCapitals.remove(newCity);
                                            capital.setChosen(true);
                                            bestCapitals.add(capital);
                                            Collections.sort(bestCapitals, new ComparatorByMoney());
                                            for(j = 0; j<MAX; j++){
                                                //System.out.println( String.format("%s",bestCapitals.get(j).getName()) );
                                            }
                                            citiesD = SetMatrixDistances(bestCapitals);
                                            bestRoute(citiesD, arrayCities, bestCapitals, capitals, finalRoute/*, s, a);
                                            break;
                                        }
                                    }
                                }
                            }
                        }/*
                    }
                }

            //System.out.println("SUM ANTES: " + sum);
            sum = lessDistance + sum;


           // System.out.println("SUM DESPUES: " + sum);
            finalRoute.add(bestCapitals.get(newCity));
    //        s[newCity] = 1;
            //a[city][newCity] = 1;
            //a[newCity][city] = 1;

    //            System.out.println("Matrix s in the interation number: " + k);
    //            for (i = 0; i < MAX; i++)
    //            {
    //                System.out.println(s[i]);
    //            }

            }
            double distanceBeginEnd = CalculateDistances(finalRoute.get(0),finalRoute.get(finalRoute.size()-1));
            if(distanceBeginEnd>MIN_CITIES || distanceBeginEnd<MAX_CITIES){
                sum = sum + distanceBeginEnd;

                //System.out.println(sum);
                if(sum<=MAX_DISTANCE && sum>=MIN_DISTANCE){

                    System.out.println("");
                    System.out.println("VALID ROUTE FOUND.");
                    System.out.println("");
                    double sumaPrueba = 0;
                    int cityCounter = 1;
                    for (Capital capital : finalRoute) {
                        System.out.println("City " + cityCounter + ": " + capital.getName() + ", " + capital.getContinent());
                        sumaPrueba = sumaPrueba + capital.getMoney();
                        cityCounter++;
                    }
                    System.out.println("");
                    System.out.println("TOTAL AMOUNT OF MONEY: " + sumaPrueba);
                    System.out.println("");
                    System.out.println("The tour will have the following number of cities per continent: ");
                    System.out.println("Europe: " + numCities(bestCapitals, "Europe"));
                    System.out.println("North America: " + numCities(bestCapitals, "North America"));
                    System.out.println("Central & South America: " + (numCities(bestCapitals, "Central America")+numCities(bestCapitals, "South America")));
                    System.out.println("Asia: " + numCities(bestCapitals, "Asia"));
                    System.out.println("Africa: " + numCities(bestCapitals, "Africa"));
                    System.out.println("Antarctica & Australia: " + (numCities(bestCapitals, "Antarctica")+numCities(bestCapitals, "Australia")));
                }
                else{
                    //Quitamos la ciudad con menos dinero y la cambiamos por la siguiente en la lista general
                    //System.out.println("no es valida asi que entramos aqui a ver que pasa");
                    int prueba = 0;
                    for(i=bestCapitals.size()-1;i>=0;i--){
                        //System.out.println("cambiando capitales");
                        for (Capital capital : capitals) {
                                if( capital.getContinent().equals( bestCapitals.get(i).getContinent() ) && capital.isChosen() == false){
                                    bestCapitals.remove(i);
                                    capital.setChosen(true);
                                    bestCapitals.add(capital);
                                    Collections.sort(bestCapitals, new ComparatorByMoney());
                                    /*for(j = 0; j<MAX; j++){
                                        System.out.println( String.format("%s",bestCapitals.get(j).getName()) );
                                    }
                                    citiesD = SetMatrixDistances(bestCapitals);
                                    bestRoute(citiesD, arrayCities, bestCapitals, capitals, finalRoute/*, s, a);
                                    break;
                                }
                        }

                    }
                    //System.out.println("todo recorrido + " + bestCapitals.size());

                    for(Capital c: bestCapitals){ //REVISAR
                        if(bestCapitals.size()>MIN){
                            //System.out.println("hay estas ciudades: " + bestCapitals.size());
                            if(numCities(bestCapitals,c.getContinent())>4){
                                bestCapitals.remove(c);
                                citiesD = SetMatrixDistances(bestCapitals);
                                bestRoute(citiesD, arrayCities, bestCapitals, capitals, finalRoute/*, s, a);
                            }
                        }
                    }
                    System.out.println("");
                    System.out.println("Optimizing the route has been tried. As it has been impossible to find an appropiate route, the application will generate a random solution.");

                    do{
                        System.out.println("");
                        System.out.println("Generating random route that fits the constraints...");
                        Collections.shuffle(capitals);
                        bestCapitals.clear();
                        for(Capital c: capitals){
                            c.setChosen(false);
                        }
                        for(i=0;i<MAX;i++){
                            capitals.get(i).setChosen(true);
                            bestCapitals.add(capitals.get(i));
                        }
                        //System.out.println(minMaxBestCapitals(bestCapitals));
                    }while(minMaxBestCapitals(bestCapitals)!=true);
                    citiesD = SetMatrixDistances(bestCapitals);
                    bestRoute(citiesD, arrayCities, bestCapitals, capitals, finalRoute);
                    for(i=1;i<=MAX;i++){
                    for (Capital capital : bestCapitals) {
                        prueba++;
                        System.out.println(prueba);
                        System.out.println(bestCapitals.get(MAX-1).getContinent());
                        System.out.println(capital.getContinent());
                        System.out.println(capital.isChosen());
                        if(capital.isChosen()==false && bestCapitals.get(MAX-1).getContinent().equals(capital.getContinent())){
                            bestCapitals.remove(MAX-1);
                            capital.setChosen(true);
                            bestCapitals.add(capital);
                            System.out.println("voy a ejecutar de nuevo");
                            for(j = 0; j<MAX; j++){
                                System.out.println( String.format("%s",bestCapitals.get(j).getName()) );
                            }
                            citiesD = SetMatrixDistances(bestCapitals);
                            bestRoute(citiesD, arrayCities, bestCapitals, capitals, finalRoute, s, a);
                        }
                    }

                }
                System.exit(0);
            }
            else{
                for (Capital capital : capitals) {
                    if( capital.getContinent().equals(finalRoute.get(finalRoute.size()-1).getContinent() ) && capital.isChosen() == false){
                        bestCapitals.remove(finalRoute.get(finalRoute.size()-1));
                        capital.setChosen(true);
                        bestCapitals.add(capital);
                        citiesD = SetMatrixDistances(bestCapitals);
                        bestRoute(citiesD, arrayCities, bestCapitals, capitals, finalRoute/*, s, a);
                        break;
                    }
                }
            }
        }


        public static double Radians(double x){
            return (Math.PI / 180) * x;
        }

        static int numCities(ArrayList <Capital> arrayCities, String continente){
            int cont = 0;
            for(Capital capital: arrayCities){
                if(capital.getContinent().compareToIgnoreCase(continente)==0){
                    cont++;
                }
            }
            return cont;
        }

        static boolean minMaxBestCapitals(ArrayList <Capital> bestCapitals){
            /*System.out.println("eu: " + numCities(bestCapitals, "Europe"));
            System.out.println("af: " + numCities(bestCapitals, "Africa"));
            System.out.println("na: " + numCities(bestCapitals, "North America"));
            System.out.println("ca: " + numCities(bestCapitals, "Central America"));
            System.out.println("sa: " + numCities(bestCapitals, "South America"));
            System.out.println("as: " + numCities(bestCapitals, "Asia"));
            System.out.println("an: " + numCities(bestCapitals, "Antarctica"));
            System.out.println("au: " + numCities(bestCapitals, "Australia"));
            if(numCities(bestCapitals, "Europe")>=4 && numCities(bestCapitals, "Europe")<=9){
                if(numCities(bestCapitals, "Asia")>=4 && numCities(bestCapitals, "Asia")<=9){
                    if(numCities(bestCapitals, "Africa")>=4 && numCities(bestCapitals, "Africa")<=9){
                        if(numCities(bestCapitals, "North America")>=4 && numCities(bestCapitals, "North America")<=9){
                            if((numCities(bestCapitals, "Central America")+numCities(bestCapitals, "South America"))>=4 && (numCities(bestCapitals, "Central America")+numCities(bestCapitals, "South America"))<=9){
                                if((numCities(bestCapitals, "Australia")+numCities(bestCapitals, "Antarctica"))>=4 && (numCities(bestCapitals, "Australia")+numCities(bestCapitals, "Antarctica"))<=9){
                                    return true;
                                }
                                else{
                                    return false;
                                }
                            }
                            else{
                                return false;
                            }
                        }
                        else{
                            return false;
                        }
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }

        static double CalculateDistances(Capital capital1, Capital capital2){ //Funcion que calcula las distancias
            double earthRadius = 6378.0;
            double posLatitude1Rad =  Radians(capital1.getLatitude());
            double posLatitude2Rad = Radians(capital2.getLatitude());
            double posLongitude1Rad = Radians(capital1.getLongitude());
            double posLongitude2Rad = Radians(capital2.getLongitude());

            double difLatitude =  Math.abs(posLatitude1Rad - posLatitude2Rad);
            double difLongitude = Math.abs(posLongitude1Rad - posLongitude2Rad);

            double a = Math.pow( Math.sin(difLatitude/2), 2 ) +
                    Math.cos(posLatitude1Rad) *
                    Math.cos(posLatitude2Rad) *
                    Math.pow( Math.sin(difLongitude/2), 2);

            double c = 2 * Math.atan2( Math.sqrt(a), Math.sqrt(1-a) );

            return earthRadius * c;
        }

        static double[][] SetMatrixDistances(ArrayList<Capital> bestCapitals){

            double[][] cities = new double[MAX][MAX];
            double distance;
            int i = 0,j;

            for ( i = 0; i<bestCapitals.size(); i++) {

                for ( j = 0; j < bestCapitals.size(); j++) {

                    distance = CalculateDistances(bestCapitals.get(i),bestCapitals.get(j));

                    cities[i][j] = distance;
                }
            }

            return cities;

        }

        }



    */
    


    //SEGUNDO BUCLE DE COMPROBACION.
        /*int city=0, newCity=0, k;
            double lessDistance, sum = 0;

            for (k=1;k<MAX;k++)
            {
                lessDistance = 100000000;
                //Console.WriteLine("k: " + k);
                /*for (j = 0; j < MAX; j++)
                {

                    //Console.WriteLine("j: " + j);
                    if (j == lastAdded) {
                        city = newCity;
                        for (i = 0; i < MAX; i++)
                        {
                            if (citiesD[city][i] < lessDistance && city != i && s[i]==0)
                            {
                                lessDistance = citiesD[city][i];
                                newCity = i;
                                System.out.println(lessDistance);
                                System.out.println(arrayCities.get(city));
                                System.out.println(arrayCities.get(i));
                                if(lessDistance<MIN_CITIES || lessDistance>MAX_CITIES){
                                    for (Capital capital : capitals) {
                                        if( capital.getContinent().equals( bestCapitals.get(newCity).getContinent() ) && capital.isChosen() == false){
                                            bestCapitals.remove(newCity);
                                            capital.setChosen(true);
                                            bestCapitals.add(capital);
                                            
                                        }
                                    }
                                    System.out.println("ERROR");
                                    System.exit(1);
                                }
                                //Console.WriteLine("newCity: " + i);
                            }/*
                        }
                    }f
                }
                        System.out.println(sum);
               sum = lessDistance + sum;
                s[newCity] = 1;
                a[city][newCity] = 1;
                a[newCity][city] = 1;

                System.out.println("Matrix s in the interation number: " + k);
                for (i = 0; i < MAX; i++)
                {
                    System.out.println(s[i]);
                }
            }
            
            if(sum<=MAX_DISTANCE){
                System.out.println("RUTA VALIDA");
            }*/

            /*
            for (i = 0; i < MAX; i++)
            {
                for (j = 0; j < MAX; j++)
                {
                    System.out.println(a[i][j]);
                }
            }
            */


//PRIMER BUCLE COMPROBACION DE CIUDADES CORRECTAS. DISTANCIAS ETC.
/*
    
    
    static boolean CheckDistances(double[][] citiesD){ //De esta forma sabemos que ciudad no cumple los requisitos
        
        int[] s = new int[MAX];
        double menor;
        int tempi, tempj;
        
        for (int i : s) { i = 0; }
        
        s[0] = 1;
        
        for(int k = 0; k < MAX-1; k++){
            menor = MAX; tempi = 0; tempj = 0;
            for (int i = 0; i < MAX; i++) {
                for( int j = 0; j < MAX_CAPITALS; i++){
                    if(s[i] == 1 && s[j] == 0 && citiesD[i][j] < menor && i != j && citiesD[i][j] > MIN_DISTANCE && citiesD[i][j] < MAX_DISTANCE){ 
                        menor = citiesD[i][j];
                        tempi = i;
                        tempj = j;
                    } else { return false; }
                }
                s[tempj] = 1;
            }
            for (int i : s) {
                System.out.printf("%d ", s[i]);
            }
            System.out.printf( "\n\n");
        }
        return true;
    }
    */


//Esto no se que es xD
/*
        int num = 0;
        System.out.println(capitals.size());
        for(Capital c:capitals){
            if((c.getContinent().equalsIgnoreCase("Europe"))==true){
                num++;
            }
        }
        System.out.println("Europa: " + num);
        
        num = 0;
        System.out.println(capitals.size());
        for(Capital c:capitals){
            if((c.getContinent().equalsIgnoreCase("Africa"))==true){
                num++;
            }
        }
        System.out.println("Africa: " + num);
        
        num = 0;
        System.out.println(capitals.size());
        for(Capital c:capitals){
            if((c.getContinent().equalsIgnoreCase("Asia"))==true){
                num++;
            }
        }
        System.out.println("Asia: " + num);
        
        num = 0;
        System.out.println(capitals.size());
        for(Capital c:capitals){
            if((c.getContinent().equalsIgnoreCase("North America"))==true || (c.getContinent().equalsIgnoreCase("Central America"))==true){
                num++;
            }
        }
        
        System.out.println("America Norte y central: " + num);
        
        num = 0;
        System.out.println(capitals.size());
        for(Capital c:capitals){
            if((c.getContinent().equalsIgnoreCase("South America"))==true){
                num++;
            }
        }
        System.out.println("America Sur: " + num);
        
        num = 0;
        System.out.println(capitals.size());
        for(Capital c:capitals){
            if((c.getContinent().equalsIgnoreCase("Australia"))==true || (c.getContinent().equalsIgnoreCase("Antarctica"))==true){
                num++;
            }
        }
        System.out.println("Australia: " + num);
                */
