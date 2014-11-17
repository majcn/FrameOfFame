/* LIBGIMP - The GIMP Library
 * Copyright (C) 1995-1997 Peter Mattis and Spencer Kimball
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

#include "gimpcolorspace.h"
#include "macros.h"


/**
 * gimp_rgb_to_hsl_int:
 * @red: Red channel, returns Hue channel
 * @green: Green channel, returns Lightness channel
 * @blue: Blue channel, returns Saturation channel
 *
 * The arguments are pointers to int representing channel values in the
 * RGB colorspace, and the values pointed to are all in the range [0, 255].
 *
 * The function changes the arguments to point to the corresponding HLS
 * value with the values pointed to in the following ranges:  H [0, 360],
 * L [0, 255], S [0, 255].
 **/
void
gimp_rgb_to_hsl_int (int *red,
                     int *green,
                     int *blue)
{
    int    r, g, b;
    double h, s, l;
    int    min, max;
    int    delta;

    r = *red;
    g = *green;
    b = *blue;

    if (r > g)
    {
        max = MAX (r, b);
        min = MIN (g, b);
    }
    else
    {
        max = MAX (g, b);
        min = MIN (r, b);
    }

    l = (max + min) / 2.0;

    if (max == min)
    {
        s = 0.0;
        h = 0.0;
    }
    else
    {
        delta = (max - min);

        if (l < 128)
            s = 255 * (double) delta / (double) (max + min);
        else
            s = 255 * (double) delta / (double) (511 - max - min);

        if (r == max)
            h = (g - b) / (double) delta;
        else if (g == max)
            h = 2 + (b - r) / (double) delta;
        else
            h = 4 + (r - g) / (double) delta;

        h = h * 42.5;

        if (h < 0)
            h += 255;
        else if (h > 255)
            h -= 255;
    }

    *red   = ROUND (h);
    *green = ROUND (s);
    *blue  = ROUND (l);
}

/**
 * gimp_rgb_to_l_int:
 * @red: Red channel
 * @green: Green channel
 * @blue: Blue channel
 *
 * Calculates the lightness value of an RGB triplet with the formula
 * L = (max(R, G, B) + min (R, G, B)) / 2
 *
 * Return value: Luminance vaue corresponding to the input RGB value
 **/
int
gimp_rgb_to_l_int (int red,
                   int green,
                   int blue)
{
    int min, max;

    if (red > green)
    {
        max = MAX (red,   blue);
        min = MIN (green, blue);
    }
    else
    {
        max = MAX (green, blue);
        min = MIN (red,   blue);
    }

    return ROUND ((max + min) / 2.0);
}

static inline int
gimp_hsl_value_int (double n1,
                    double n2,
                    double hue)
{
    double value;

    if (hue > 255)
        hue -= 255;
    else if (hue < 0)
        hue += 255;

    if (hue < 42.5)
        value = n1 + (n2 - n1) * (hue / 42.5);
    else if (hue < 127.5)
        value = n2;
    else if (hue < 170)
        value = n1 + (n2 - n1) * ((170 - hue) / 42.5);
    else
        value = n1;

    return ROUND (value * 255.0);
}

/**
 * gimp_hsl_to_rgb_int:
 * @hue: Hue channel, returns Red channel
 * @saturation: Saturation channel, returns Green channel
 * @lightness: Lightness channel, returns Blue channel
 *
 * The arguments are pointers to int, with the values pointed to in the
 * following ranges:  H [0, 360], L [0, 255], S [0, 255].
 *
 * The function changes the arguments to point to the RGB value
 * corresponding, with the returned values all in the range [0, 255].
 **/
void
gimp_hsl_to_rgb_int (int *hue,
                     int *saturation,
                     int *lightness)
{
    double h, s, l;

    h = *hue;
    s = *saturation;
    l = *lightness;

    if (s == 0)
    {
        /*  achromatic case  */
        *hue        = l;
        *lightness  = l;
        *saturation = l;
    }
    else
    {
        double m1, m2;

        if (l < 128)
            m2 = (l * (255 + s)) / 65025.0;
        else
            m2 = (l + s - (l * s) / 255.0) / 255.0;

        m1 = (l / 127.5) - m2;

        /*  chromatic case  */
        *hue        = gimp_hsl_value_int (m1, m2, h + 85);
        *saturation = gimp_hsl_value_int (m1, m2, h);
        *lightness  = gimp_hsl_value_int (m1, m2, h - 85);
    }
}
