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

#include "hue-saturation.h"
#include "macros.h"


void
hue_saturation_init (HueSaturation *hs,
                     double         hue,
                     double         lightness,
                     double         saturation)
{
    hs->hue = hue;
    hs->lightness = lightness;
    hs->saturation = saturation;
}

void
hue_saturation_setup (HueSaturation *hs)
{
    int value;
    int i;

    /*  Calculate transfers  */
    for (i = 0; i < 256; i++)
    {
        /*  Hue  */
        value = (hs->hue) * 255.0 / 360.0;
        if ((i + value) < 0)
            hs->hue_transfer[i] = 255 + (i + value);
        else if ((i + value) > 255)
            hs->hue_transfer[i] = i + value - 255;
        else
            hs->hue_transfer[i] = i + value;

        /*  Lightness  */
        value = (hs->lightness) * 127.0 / 100.0;
        value = CLAMP (value, -255, 255);

        if (value < 0)
            hs->lightness_transfer[i] = (unsigned char) ((i * (255 + value)) / 255);
        else
            hs->lightness_transfer[i] = (unsigned char) (i + ((255 - i) * value) / 255);

        /*  Saturation  */
        value = (hs->saturation) * 255.0 / 100.0;
        value = CLAMP (value, -255, 255);

        /* This change affects the way saturation is computed. With the
           old code (different code for value < 0), increasing the
           saturation affected muted colors very much, and bright colors
           less. With the new code, it affects muted colors and bright
           colors more or less evenly. For enhancing the color in photos,
           the new behavior is exactly what you want. It's hard for me
           to imagine a case in which the old behavior is better.
         */
        hs->saturation_transfer[i] = CLAMP0255 ((i * (255 + value)) / 255);
    }
}

void
hue_saturation_process (HueSaturation     *hs,
                        RGBA              *rgba)
{
    int r, g, b, r_n, g_n, b_n;

    r = rgba->r;
    g = rgba->g;
    b = rgba->b;

    gimp_rgb_to_hsl_int (&r, &g, &b);
    r_n = hs->hue_transfer[r];
    g_n = hs->saturation_transfer[g];
    b_n = hs->lightness_transfer[b];
    gimp_hsl_to_rgb_int (&r_n, &g_n, &b_n);

    rgba->r = CLAMP0255 (r_n);
    rgba->g = CLAMP0255 (g_n);
    rgba->b = CLAMP0255 (b_n);
}
