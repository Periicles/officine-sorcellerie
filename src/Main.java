public class Main {
    public static void main(String[] args) {
        Officine officine = new Officine();
        officine.rentrer("5 larmes de brume funèbre");
        officine.rentrer("2 gouttes de sang de citrouille");
        int preparees = officine.preparer("2 fioles de glaires purulentes");
        System.out.println("Fioles préparées : " + preparees);
        System.out.println("Stock de larmes : " + officine.quantite("larme de brume funèbre"));
    }
}
