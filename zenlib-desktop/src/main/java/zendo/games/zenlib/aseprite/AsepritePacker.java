package zendo.games.zenlib.aseprite;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.backends.headless.HeadlessNativesLoader;
import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.PixmapPackerIO;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.mock;

public class AsepritePacker {

    private static final String tag = AsepritePacker.class.getSimpleName();

    public AsepritePacker() {
        HeadlessNativesLoader.load();
        Gdx.graphics = new MockGraphics();
        Gdx.files = new HeadlessFiles();
        Gdx.gl = mock(GL20.class);
    }

    public static void main(String... args) {
        String input = null;
        String output = null;
        String packFileName = "pack.atlas";

        // parse out pack params from args
        switch (args.length) {
            case 3: packFileName = args[2];
            case 2: output       = args[1];
            case 1: input        = args[0];
            break;
            default: {
                System.out.println(tag + " Usage: inputDir [outputDir] [packFileName]");
                System.exit(0);
            }
        }

        // convert output dir to absolute path
        // TODO: since we're using Gdx.files, this probably isn't necessary
        //  unless we want to allow output to any arbitrary dir outside the project
        if (output == null) {
            File inputFile = new File(input);
            output = new File(inputFile.getParentFile(), inputFile.getName() + "-packed").getAbsolutePath();
        } else {
            output = new File(output).getAbsolutePath();
        }

        System.out.println("Params:\n\tinputDir = " + input + "\n\toutputDir = " + output + "\n\tpackFileName = " + packFileName);

        try {
            AsepritePacker packer = new AsepritePacker();
            packer.process(input, output, packFileName);
        } catch (IOException e) {
            System.err.println(tag + ": Failed to pack atlas from aseprite files\n" + e.getMessage());
        }
    }

    private void process(String inputDir, String output, String packFileName) throws IOException {
        // configure a pixmap packer
        // TODO: maybe optionally pass some of these as args?
        int pageWidth = 1024;
        int pageHeight = 1024;
        Pixmap.Format pageFormat = Pixmap.Format.RGBA8888;
        int padding = 0;
        boolean duplicateBorder = false;
        boolean stripWhitespaceX = false;
        boolean stripWhitespaceY = false;
        PixmapPacker.PackStrategy packStrategy = new PixmapPacker.GuillotineStrategy();
        PixmapPacker packer = new PixmapPacker(
                pageWidth, pageHeight, pageFormat, padding,
                duplicateBorder, stripWhitespaceX, stripWhitespaceY,
                packStrategy);

        // load aseprite files and pack animation frame pixmaps
        for (FileHandle aseFile : Gdx.files.internal(inputDir).list(".ase")) {
            Aseprite.loadAndPack(packer, inputDir + aseFile.name());
        }

        // write out texture atlas files to system
        FileHandle outFileHandle = Gdx.files.getFileHandle(output + "/" + packFileName, Files.FileType.Absolute);
        PixmapPackerIO packerIO = new PixmapPackerIO();
        PixmapPackerIO.SaveParameters saveParams = new PixmapPackerIO.SaveParameters();
        saveParams.useIndexes = true; // note - defaults are fine, except we do want to use indexes
        packerIO.save(outFileHandle, packer, saveParams);
    }

}
