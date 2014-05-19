/* GIMP - The GNU Image Manipulation Program
 * Copyright (C) 1995 Spencer Kimball and Peter Mattis
 *
 * gimplut.c: Copyright (C) 1999 Jay Cox <jaycox@gimp.org>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

#include "gimplut.h"
#include "macros.h"
#include <math.h>


static float
posterize_lut_func (int    levels,
                    float  value)
{
  return floor ((value * (levels - 1.0)) + 0.5) / (levels - 1.0);
}

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

  i = 256;
  while(i--)
    {
      val = 255.0 * posterize_lut_func(lut->levels, i/255.0) + 0.5;
      lut->luts[i] = CLAMP0255 (val);
    }
}

void
gimp_lut_process (GimpLut           *lut,
                  AndroidBitmapInfo *info,
                  void              *pixels)
{
  int xx, yy, r, g, b, r_n, g_n, b_n;
  uint32_t* line;

  for(yy = 0; yy < info->height; yy++) {
    line = (uint32_t*)pixels;
    for(xx =0; xx < info->width; xx++) {
      r = (int) ((line[xx] & 0x00FF0000) >> 16);
      g = (int) ((line[xx] & 0x0000FF00) >> 8);
      b = (int) (line[xx] & 0x00000FF );

      r_n = lut->luts[r];
      g_n = lut->luts[g];
      b_n = lut->luts[b];

      line[xx] = ((r_n << 16) & 0x00FF0000) | ((g_n << 8) & 0x0000FF00) | (b_n & 0x000000FF);
    }
    pixels = (char*)pixels + info->stride;
  }
}
