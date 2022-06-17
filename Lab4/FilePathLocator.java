package Lab4;

public enum FilePathLocator {
    FILE_IN ("src/main/java/Lab4New/input.txt");

    private final String path;

    FilePathLocator(String path){this.path = path;}

    public String getPath(){return path;}
}
