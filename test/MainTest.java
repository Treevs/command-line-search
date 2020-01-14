import com.company.Main;
import org.junit.Assert;
import org.junit.Test;

public class MainTest {
    @Test
    public void mainTest() {
        Assert.assertEquals(Main.getUserNameById("19"), "Francis Rodrigüez");
        Assert.assertEquals(Main.getOrganizationNameById("109"), "Möreganic");
        String[] ticketArr = {"A Problem in Zaire", "A Nuisance in Togo"};
        Assert.assertArrayEquals(Main.getTicketsBySubmitterId("19"), ticketArr);
        //Good search file
        Main.searchJsonFile("docs/users.json", "1", "_id", "19");
        //Bad file
        Main.searchJsonFile("docs/uusers.json", "1", "_id", "19");
        //Bad search num
        Main.searchJsonFile("docs/users.json", "100", "_id", "19");
        //Invalid search num
        Main.searchJsonFile("docs/users.json", "X", "_id", "19");
        //Bad search term
        Main.searchJsonFile("docs/users.json", "1", "_idog", "19");
        //Bad search value
        Main.searchJsonFile("docs/users.json", "1", "_id", "Frank");
    }
}
