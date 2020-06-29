package fr.mimich.pala;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;


public final class Main {

    public static void main(String[] args) {

        if (args.length <= 0){

            System.err.println("Please use --help on options");

            return;

        }

        if (args[0].equalsIgnoreCase("--help") || args.length <= 2){

            System.out.println("Usage: java -jar PalaCipherReverser.jar <1/2> <in> <out>");

            System.out.println("Explication: 1 for .jar > .pala and 2 .pala > .jar");

            System.out.println("For <in> and <out> was just a dir for in and out path, on <in> you can set just a directory for reverse all");

            return;

        }


        reverse(args[1], args[2], Integer.parseInt(args[0]));


    }

    private static final void reverse(final String inputPath, final String outpoutPath, int mode){

        System.out.println("Starting...");

        File in, out;

        in = new File(inputPath);

        out = new File(outpoutPath);

        System.out.println("File loading...");

        final Set<File> files = new HashSet<>();


        if (in.isDirectory()){

            for (int i = 0; i < in.listFiles().length ; i++) {

                final File file = in.listFiles()[i];

                if ((mode == 1 && file.getName().endsWith(".jar")) || (mode == 2 && file.getName().endsWith(".pala"))) {

                    files.add(file);

                    System.out.println("(" + files.size() + "/" + in.listFiles().length + ") " + file.getName() + " can be reverse !");

                }


            }

        }else {

            files.add(in);

        }

        System.out.println("Files was loaded ! (" + files.size() + ")");


        for (File file : files){

            try {

                Files.write(new File(out, file.getName().split("\\.")[0] + (mode == 1 ? ".pala" : ".jar")).toPath(), cipher(file, mode));

                System.out.println(file.getName() + " was reverse. Enjoy !");

            } catch (IOException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException e) {

                e.printStackTrace();

                System.err.println("Can make a process bye !");

                System.exit(1);

            }

        }

    }

    private static byte[] cipher(final File file, int mode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {

        //get this key from a web request on a paladium server.
        final String KEY = "dK5rcm5RuMBETH5C";


            final SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), "AES");

            final Cipher cipher = Cipher.getInstance("AES");

            cipher.init(mode, secretKey);

            FileInputStream inputStream = new FileInputStream(file);

            byte[] inputBytes = new byte[(int) file.length()];

            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            inputStream.close();

            return outputBytes;

    }


}
