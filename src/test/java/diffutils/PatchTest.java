package diffutils;

import difflib.DiffUtils;
import difflib.Patch;
import difflib.PatchFailedException;
import difflib.StringUtills;
import junit.framework.TestCase;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class PatchTest extends TestCase {

    public void testPatch_Insert()  {
        final List<String> insertTest_from = Arrays.asList("hhh");
        final List<String> insertTest_to = Arrays.asList("hhh", "jjj", "kkk", "lll");

        final Patch<String> patch = DiffUtils.diff(insertTest_from, insertTest_to);
        try {
            assertEquals(insertTest_to, DiffUtils.patch(insertTest_from, patch));
        } catch (PatchFailedException e) {
            fail(e.getMessage());
        }
    }

    public void testPatch_Delete() {
        final List<String> deleteTest_from = Arrays.asList("ddd", "fff", "ggg", "hhh");
        final List<String> deleteTest_to = Arrays.asList("ggg");

        final Patch<String> patch = DiffUtils.diff(deleteTest_from, deleteTest_to);
        try {
            assertEquals(deleteTest_to, DiffUtils.patch(deleteTest_from, patch));
        } catch (PatchFailedException e) {
            fail(e.getMessage());
        }
    }

    public void testPatch_Change() {
        final List<String> changeTest_from = Arrays.asList("aaa", "bbb", "ccc", "ddd");
        final List<String> changeTest_to = Arrays.asList("aaa", "bxb", "cxc", "ddd");

        final Patch<String> patch = DiffUtils.diff(changeTest_from, changeTest_to);
        try {
            assertEquals(changeTest_to, DiffUtils.patch(changeTest_from, patch));
        } catch (PatchFailedException e) {
            fail(e.getMessage());
        }
    }

    public void testPatch_Serialize() {
        final List<String> changeTest_from = Arrays.asList("aaa", "bbb", "ccc", "ddd");
        final List<String> changeTest_to = Arrays.asList("aaa", "bxb", "cxc", "ddd");

        final Patch<String> patch = DiffUtils.diff(changeTest_from, changeTest_to);

        final String tempFile = StringUtills.join(Arrays.asList("src", "test", "resources", "serialize_test.ser"), File.separator);

        try {
            FileOutputStream fos = new FileOutputStream(tempFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(patch);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail("file not found");
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException");
        }

        Patch restored = null;
        try {
            FileInputStream fis = new FileInputStream(tempFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            restored = (Patch) ois.readObject();
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail("fail not found");
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(patch, restored);
    }
}
