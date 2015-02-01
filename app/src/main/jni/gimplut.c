/* GIMP - The GNU Image Manipulation Program
 * Copyright (C) 1995 Spencer Kimball and Peter Mattis
 *
 * gimplut.c: Copyright (C) 1999 Jay Cox <jaycox@gimp.org>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

#include "gimplut.h"
#include "macros.h"
#include <math.h>


void
gimp_lut_init (GimpLut *lut,
               int      levels)
{
    lut->levels = levels;
}

void
gimp_lut_setup (GimpLut *lut)
{
    int i;
    double val;

    for (i = 0; i < 256; i++)
    {
        val = 255.0 * floor (((i / 255.0) * (lut->levels - 1.0)) + 0.5) / (lut->levels - 1.0) + 0.5;
        lut->luts[i] = CLAMP0255 (val);
    }
}

void
gimp_lut_process (GimpLut           *lut,
                  AndroidBitmapInfo *info,
                  void              *pixels)
{
    int xx, yy, r, g, b, r_n, g_n, b_n, pixel;
    unsigned short* picbuf = pixels;

    for (yy = 0; yy < info->height; yy++)
    {
        for (xx = 0; xx < info->width; xx++)
        {
            pixel = picbuf[yy * info->width + xx];
            r = RGB565_R(pixel);
            g = RGB565_G(pixel);
            b = RGB565_B(pixel);

            r_n = lut->luts[r];
            g_n = lut->luts[g];
            b_n = lut->luts[b];

            picbuf[yy * info->width + xx] = MAKE_RGB565(r_n, g_n, b_n);
        }
    }
}
