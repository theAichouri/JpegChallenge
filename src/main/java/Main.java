
import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageResponse;
import com.byteowls.jopencage.model.JOpenCageReverseRequest;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
Mohamed Aichouri
maichour@calpoly.edu
 */
public class Main {

    /*
    A portable application that can validate an image is a jpeg and find out where that image was
    taken and return the zip code it was taken in.
     */
    public static void main(String[] args) throws Exception {

        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int openOutcome = jfc.showOpenDialog(null);
        if(openOutcome != JFileChooser.APPROVE_OPTION) {
            System.exit(0);
        }
        File myFile = SelectFile(jfc.getSelectedFile());
        assert myFile != null;
        Boolean status = isJPEG(myFile);
        if(status) {
            System.out.println("This file is a JPEG");
            getLocation(myFile.toString());
        }
        else{
            System.out.println("This file is not a JPEG, Please try choosing a different file");
            JOptionPane.showMessageDialog(null,
                    "This file is not a JPEG, Please try choosing a different file",
                    "JPegChallenge",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /*
    getLocation method takes in a file Path to a Jpeg image (as a String), gets the Latitude and Longitude coordinate
    from the metadata of the Jpeg image (if they exist). then use these coordinates to get the full address linked to
    these coordinates.
    I am using the https://opencagedata.com/api to retrieve the full address where the Jpeg image was located.
     */
    public static void getLocation (String path) {
        javaxt.io.Image image = new javaxt.io.Image(path);
        double[] coordinates;
        if((coordinates = image.getGPSCoordinate()) == null) {
            System.out.println("this file does not contain GPS coordinates, Please try choosing a different file");
            JOptionPane.showMessageDialog(null,
                    "This file does not contain GPS coordinates, Please try choosing a different files",
                    "JPegChallenge",
                    JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        //This api provides a java client to the OpenCage geocoding service
        JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder("13adad428aaa4c92979ad5f60528685f" );
        JOpenCageReverseRequest request = new JOpenCageReverseRequest(coordinates[1], coordinates[0]);
        request.setNoAnnotations(true);
        JOpenCageResponse response = jOpenCageGeocoder.reverse(request);

        String formattedAddress = response.getResults().get(0).getFormatted();
        String zipCode = getZipCode(formattedAddress);
        if(zipCode == null) {
            System.out.println("The address is not a US address or the zipCode is not valid");
            JOptionPane.showMessageDialog(null,
                    "The address is not a US address or the zipCode is not valid",
                    "JPegChallenge",
                    1);
        }
        else{
            System.out.println("ZipCode: "+ zipCode);
            JOptionPane.showMessageDialog(null,"This is a Jpeg file taken at zipCode :"
                            +zipCode+ "\n" + "The full address is:"+formattedAddress,"JPegChallenge",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        System.out.println("Location of the Jpeg: "+ formattedAddress);
    }

    /*
    This function takes is an address as a String, checks for a Valid US ZipCode and returns it.
     */
    public static String getZipCode(String formattedAddress) {
        String[] tokens = formattedAddress.split("[ ,]");
        String regex = "^[0-9]{5}(?:-[0-9]{4})?$";
        Pattern pattern = Pattern.compile(regex);

        for (String s:tokens) {
            Matcher matcher = pattern.matcher(s);
            if(matcher.matches()) {
                return s;
            }
        }
        return null;
    }

    /*
    This is a helper function for JFileChooser that takes a file type and check if it is a Directory or a file.
    if it is a file, I return it to main, otherwise if it is a directory I list all the files instead that
    Directory
     */
    public static File SelectFile (File file) throws Exception {
        if(file.isFile()) {
            return file;
        }
        else {
            File [] files = file.listFiles();
            assert files != null;
            for (File f : files) {
                if(f.isDirectory()) {
                    SelectFile(f);
                }
                else {
                    return file;
                }
            }
        }
        return null;
    }
    /*
    JPEG file signatures (data used to identify or verify the content of a file) are:
    0xffd8ffe0 ,0xffd8ffe1, 0xffd8ffee, and 0xffd8ffdb.
    Such signatures are also known as magic numbers or Magic Bytes
    I used these file signature to Identify the Jpeg files.
     */
    public static Boolean isJPEG(File filename) throws Exception {
        try (DataInputStream ins = new DataInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            return ins.readInt() == 0xffd8ffe0 || ins.readInt() == 0xffd8ffe1
                    || ins.readInt() == 0xffd8ffee || ins.readInt() == 0xffd8ffdb;
        }
    }
}
