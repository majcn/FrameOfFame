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

#include "color-balance.h"
#include "macros.h"
#include "gimpcolorspace.h"


/*  local function prototypes  */

static void color_balance_transfer_init (void);


/*  private variables  */

static int transfer_initialized = 0;

/*  for lightening  */
static double shadows_add[256] = { 0 };

/*  for darkening  */
static double shadows_sub[256] = { 0 };


/*  public functions  */

void
color_balance_init (ColorBalance *cb,
                    double        c_r,
                    double        m_g,
                    double        y_b)
{
  cb->cyan_red = c_r;
  cb->magenta_green = m_g;
  cb->yellow_blue = y_b;
}

void
color_balance_setup (ColorBalance *cb)
{
  double *cyan_red_transfer;
  double *magenta_green_transfer;
  double *yellow_blue_transfer;
  int i;
  signed int r_n, g_n, b_n;

  if (transfer_initialized == 0)
    {
      color_balance_transfer_init ();
      transfer_initialized = 1;
    }

  /*  Prepare the transfer arrays  (for speed)  */

  cyan_red_transfer = (cb->cyan_red > 0) ? shadows_add : shadows_sub;
  magenta_green_transfer = (cb->magenta_green > 0) ? shadows_add : shadows_sub;
  yellow_blue_transfer = (cb->yellow_blue > 0) ? shadows_add : shadows_sub;

  i = 256;
  while(i--)
    {
      r_n = i;
      g_n = i;
      b_n = i;

      r_n += (cb->cyan_red * cyan_red_transfer[r_n]);
      r_n = CLAMP0255 (r_n);

      g_n += (cb->magenta_green * magenta_green_transfer[g_n]);
      g_n = CLAMP0255 (g_n);

      b_n += (cb->yellow_blue * yellow_blue_transfer[b_n]);
      b_n = CLAMP0255 (b_n);

      cb->r_lookup[i] = r_n;
      cb->g_lookup[i] = g_n;
      cb->b_lookup[i] = b_n;
    }
}

void
color_balance_process (ColorBalance      *cb,
                       AndroidBitmapInfo *info,
                       void              *pixels)
{
	int xx, yy, r, g, b, r_n, g_n, b_n;
	uint32_t *line;

	for(yy = 0; yy < info->height; yy++) {
		line = (uint32_t*)pixels;
		for(xx =0; xx < info->width; xx++) {
            r = (int) ((line[xx] & 0x00FF0000) >> 16);
			g = (int) ((line[xx] & 0x0000FF00) >> 8);
			b = (int) (line[xx] & 0x00000FF );

            r_n = cb->r_lookup[r];
            g_n = cb->g_lookup[g];
            b_n = cb->b_lookup[b];

            gimp_rgb_to_hsl_int (&r_n, &g_n, &b_n);
            b_n = gimp_rgb_to_l_int (r, g, b);
            gimp_hsl_to_rgb_int (&r_n, &g_n, &b_n);

            line[xx] = ((r_n << 16) & 0x00FF0000) | ((g_n << 8) & 0x0000FF00) | (b_n & 0x000000FF);
		}
		pixels = (char*)pixels + info->stride;
	}
}


/*  private functions  */

static void
color_balance_transfer_init (void)
{
  int i=256;
  while(i--)
    {
      shadows_add[i] = 0.667 * (1 - SQR (((double) i - 127.0) / 127.0)); //(1.075 - 1 / ((double) i / 16.0 + 1));
      shadows_sub[255 - i] = (1.075 - 1 / ((double) i / 16.0 + 1));
    }
}
