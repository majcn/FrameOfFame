/* GIMP - The GNU Image Manipulation Program
 * Copyright (C) 1995 Spencer Kimball and Peter Mattis
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

#ifndef __HUE_SATURATION_H__
#define __HUE_SATURATION_H__

#include <android/bitmap.h>



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
                              AndroidBitmapInfo *info,
                              void              *pixels);


#endif  /*  __HUE_SATURATION_H__  */
