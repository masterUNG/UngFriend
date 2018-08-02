package masterung.androidthai.in.th.ungfriend.utility;

public class MyConstant {

    private String hostFtpString = "ftp.androidthai.in.th";
    private String userFtpString = "pae@androidthai.in.th";
    private String passwordFtpString = "Abc12345";
    private int portFtpAnInt = 21;

    private String urlAddUserString = "http://androidthai.in.th/pae/addUserMaster.php";
    private String urlReadAllUser = "http://androidthai.in.th/pae/getAllUserMaster.php";

    private String[] columnNameStrings = new String[]{"id", "Name", "User", "Password", "Image", "Message"};

    public String[] getColumnNameStrings() {
        return columnNameStrings;
    }

    public String getUrlReadAllUser() {
        return urlReadAllUser;
    }

    public String getUrlAddUserString() {
        return urlAddUserString;
    }

    public String getHostFtpString() {
        return hostFtpString;
    }

    public String getUserFtpString() {
        return userFtpString;
    }

    public String getPasswordFtpString() {
        return passwordFtpString;
    }

    public int getPortFtpAnInt() {
        return portFtpAnInt;
    }
}
