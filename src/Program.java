public class Program {
    public static void main(String[] args) {
        FileService tmp = new FileService("E:\\NIC\\JavaProjects\\HexEditor\\src\\Hello.txt");

        System.out.print(tmp.getStringContent());
        System.out.println();
        System.out.print(tmp.getCharContent());
//        for (var b : tmp.getContent()){
//            System.out.print(b);
//            System.out.print(" ");
//        }
    }
}
