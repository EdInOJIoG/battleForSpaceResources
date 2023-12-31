/*
 * Copyright (C) 2018  Magnus Bull
 *
 *  This file is part of dds-lwjgl.
 *
 *  dds-lwjgl is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  dds-lwjgl is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with dds-lwjgl.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.bfsr.engine.renderer.texture.dds;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DDSPixelFormat {

    /* Flags */
    protected static final int DDPF_ALPHAPIXELS = 0x00001;
    protected static final int DDPF_ALPHA = 0x00002;
    protected static final int DDPF_FOURCC = 0x00004;
    protected static final int DDPF_RGB = 0x00040;
    protected static final int DDPF_YUV = 0x00200;
    protected static final int DDPF_LUMINANCE = 0x20000;

    /** Structure size in bytes */
    protected int dwSize;

    /** Values which indicate what type of data is in the surface */
    protected int dwFlags;

    /** Four-character code for specifying compressed or custom format */
    protected int dwFourCC;

    /** Number of bits in an RGB (possibly including alpha) format */
    protected int dwRGBBitCount;

    /** Red (or lumiannce or Y) mask for reading color data */
    protected int dwRBitMask;

    /** Green (or U) mask for reading color data */
    protected int dwGBitMask;

    /** Blue (or V) mask for reading color data */
    protected int dwBBitMask;

    /** Alpha mask for reading alpha data */
    protected int dwABitMask;

    /** Four-character code's String representation */
    protected String sFourCC;

    /** Whether this texture uses compression or not */
    protected boolean isCompressed;

    protected boolean hasFlagAlphaPixels;
    protected boolean hasFlagAlpha;
    protected boolean hasFlagFourCC;
    protected boolean hasFlagRgb;
    protected boolean hasFlagYuv;
    protected boolean hasFlagLuminance;

    protected DDSPixelFormat(ByteBuffer header, boolean printDebug) {
        dwSize = header.getInt();
        dwFlags = header.getInt();
        dwFourCC = header.getInt();
        dwRGBBitCount = header.getInt();
        dwRBitMask = header.getInt();
        dwGBitMask = header.getInt();
        dwBBitMask = header.getInt();
        dwABitMask = header.getInt();

        sFourCC = createFourCCString(dwFourCC);

        if (dwSize != 32) if (printDebug) System.err.println("Size is not 32!");

        hasFlagAlphaPixels = (dwFlags & DDPF_ALPHAPIXELS) == DDPF_ALPHAPIXELS;
        hasFlagAlpha = (dwFlags & DDPF_ALPHA) == DDPF_ALPHA;
        hasFlagFourCC = (dwFlags & DDPF_FOURCC) == DDPF_FOURCC;
        hasFlagRgb = (dwFlags & DDPF_RGB) == DDPF_RGB;
        hasFlagYuv = (dwFlags & DDPF_YUV) == DDPF_YUV;
        hasFlagLuminance = (dwFlags & DDPF_LUMINANCE) == DDPF_LUMINANCE;

        if (hasFlagFourCC) {
            isCompressed = true;
        } else if (hasFlagRgb) {
            isCompressed = false;
        }

        String sysout = "DDSPixelFormat properties:\n"
                + "\tdwSize \t\t\t= " + dwSize + "\n"
                + "\tFlags:\t\t\t";

        if (hasFlagAlphaPixels) sysout += "DDPF_ALPHAPIXELS | ";
        if (hasFlagAlpha) sysout += "DDPF_ALPHA | ";
        if (hasFlagFourCC) sysout += "DDPF_FOURCC | ";
        if (hasFlagRgb) sysout += "DDPF_RGB | ";
        if (hasFlagYuv) sysout += "DDPF_YUV | ";
        if (hasFlagLuminance) sysout += "DDPF_LUMINANCE";

        if (hasFlagFourCC) sysout += "\n\tsFourCC \t\t= " + sFourCC;
        if (hasFlagRgb) {
            sysout += "\n\tdwRGBBitCount \t= " + dwRGBBitCount
                    + "\n\tdwRBitMask \t\t= " + dwRBitMask
                    + "\n\tdwGBitMask \t\t= " + dwGBitMask
                    + "\n\tdwBBitMask \t\t= " + dwBBitMask;
        }
        sysout += "\n\tisCompressed \t= " + isCompressed;

        if (printDebug) System.out.println("\n" + sysout);
    }

    /**
     * Constructs the four-character code's String representation from the integer value.
     */
    private String createFourCCString(int fourCC) {
        byte[] fourCCString = new byte[DDPF_FOURCC];
        for (int i = 0; i < fourCCString.length; i++) fourCCString[i] = (byte) (fourCC >> (i << 3));
        return new String(fourCCString, StandardCharsets.UTF_8);
    }

}