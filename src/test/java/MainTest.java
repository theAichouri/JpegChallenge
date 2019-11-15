import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
public  class MainTest {

    @Test
    public void testIsJPEG() throws Exception{
        assertEquals(true, Main.isJPEG(new File("/Users/mohamedaichouri/IdeaProjects/JpegChallenge/src/test/Images/Zane.jpeg")));
        assertEquals(false, Main.isJPEG(new File("/Users/mohamedaichouri/IdeaProjects/JpegChallenge/src/test/Images/Simo2.heic")));
        assertEquals(true, Main.isJPEG(new File("/Users/mohamedaichouri/IdeaProjects/JpegChallenge/src/test/Images/FujiFilm.jpg")));
    }
    @Test
    public void testgetZipCode()throws Exception{
        assertEquals("93401",Main.getZipCode("3220 south Higuera street, suite 205 San Luis Obispo, CA 93401"));
        assertEquals("93422-5665",Main.getZipCode("6350 Santa Ynez Ave, Apt 6, Atascadero, CA 93422-5665"));
        assertEquals(null, Main.getZipCode("Hay Sadri Group 2, Rue 38 Numero 28, Casablanca, Morocco"));
    }
}
