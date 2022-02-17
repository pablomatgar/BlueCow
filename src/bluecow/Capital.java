package bluecow;

class Capital {
    private String name;
    private String countryName;
    private double latitude;
    private double longitude;
    private String continent;
    private double money;
    private boolean chosen;

    @Override
    public String toString() {
        return "Capital{" + "name=" + name + ", countryName=" + countryName + ", latitude=" + latitude + ", longitude=" + longitude + ", continent=" + continent + ", money=" + money + '}';
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    
    
    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double x) {
        this.latitude = x;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double y) {
        this.longitude = y;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }
    
    
}
