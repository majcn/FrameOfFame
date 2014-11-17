/* GIMP - The GNU Image Manipulation Program
 * Copyright (C) 1995 Spencer Kimball and Peter Mattis
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

#ifndef __COLOR_BALANCE_H__
#define __COLOR_BALANCE_H__

#include "rgba.h"



typedef struct
{
    double cyan_red;
    double magenta_green;
    double yellow_blue;

    unsigned char r_lookup[256];
    unsigned char g_lookup[256];
    unsigned char b_lookup[256];
} ColorBalance;


void   color_balance_init    (ColorBalance      *cb,
                              double             c_r,
                              double             m_g,
                              double             y_b);
void   color_balance_setup   (ColorBalance      *cb);
void   color_balance_process (ColorBalance      *cb,
                              RGBA              *rgba);


#endif  /*  __COLOR_BALANCE_H__  */
