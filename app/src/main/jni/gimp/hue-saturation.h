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

#ifndef __HUE_SATURATION_H__
#define __HUE_SATURATION_H__

#include "rgba.h"



typedef struct
{
    double hue;
    double lightness;
    double saturation;

    int hue_transfer[256];
    int lightness_transfer[256];
    int saturation_transfer[256];
} HueSaturation;


void   hue_saturation_init    (HueSaturation     *cb,
                               double             hue,
                               double             lightness,
                               double             saturation);
void   hue_saturation_setup   (HueSaturation     *hs);
void   hue_saturation_process (HueSaturation     *hs,
                               RGBA              *rgba);


#endif  /*  __HUE_SATURATION_H__  */
