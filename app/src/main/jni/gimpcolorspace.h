/* LIBGIMP - The GIMP Library
 * Copyright (C) 1995-1997 Peter Mattis and Spencer Kimball
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

#ifndef __GIMP_COLOR_SPACE_H__
#define __GIMP_COLOR_SPACE_H__



void    gimp_rgb_to_hsl_int     (int    *red         /* returns hue        */,
                                 int    *green       /* returns saturation */,
                                 int    *blue        /* returns lightness  */);
int     gimp_rgb_to_l_int       (int     red,
                                 int     green,
                                 int     blue);
void    gimp_hsl_to_rgb_int     (int    *hue         /* returns red        */,
                                 int    *saturation  /* returns green      */,
                                 int    *lightness   /* returns blue       */);


#endif  /* __GIMP_COLOR_SPACE_H__ */
