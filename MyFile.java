package files;

public class MyFile{
    int id;
    String name;
    byte[] data;
    String fileExtension;

    public MyFile(int id, String name, byte[] data, String fileExtension){
        this.id = id;
        this.name = name;
        this.data = data;
        this.fileExtension = fileExtension;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setData(byte[] data){
        this.data = data;
    }

    public void setFileExtension(String fileExtension){
        this.fileExtension = fileExtension;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public byte[] getData(){
        return data;
    }

    public String getFileExtenxion(){
        return fileExtension;
    }
}