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

#ifndef __GIMP_LUT_H__
#define __GIMP_LUT_H__

#include "rgba.h"



typedef struct
{
    int levels;

    unsigned char luts[256];
} GimpLut;


void   gimp_lut_init    (GimpLut           *lut,
                         int                levels);
void   gimp_lut_setup   (GimpLut           *lut);
void   gimp_lut_process (GimpLut           *lut,
                         RGBA              *rgba);


#endif /* __GIMP_LUT_H__ */
