/* 
* PROJECT: NyARToolkitCPP
* --------------------------------------------------------------------------------
*
* The NyARToolkitCPP is C++ version NyARToolkit class library.
* Copyright (C)2008-2009 Ryo Iizuka
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
* 
* For further information please contact.
*	http://nyatla.jp/nyatoolkit/
*	<airmail(at)ebony.plala.or.jp> or <nyatla(at)nyatla.jp>
* 
*/

#include "NyARMatchPattDeviationColorData.h"
#include "nyarcore.h"
#include "NyARBufferType.h"







namespace NyARToolkitCPP
{

	const int* NyARMatchPattDeviationColorData::refData()const
	{
		return this->_data;
	}
	double NyARMatchPattDeviationColorData::getPow()const
	{
		return this->_pow;
	}

	NyARMatchPattDeviationColorData::NyARMatchPattDeviationColorData(int i_width,int i_height)
	{
		this->_number_of_pixels=i_height*i_width;
		this->_data=new int[this->_number_of_pixels*3];
		this->_optimize_for_mod=this->_number_of_pixels-(this->_number_of_pixels%8);	
		return;
	}
	NyARMatchPattDeviationColorData::~NyARMatchPattDeviationColorData()
	{
		NyAR_SAFE_ARRAY_DELETE(this->_data);
		return;
	}

	/**
	* NyARRasterからパターンデータをセットします。
	* この関数は、データを元に所有するデータ領域を更新します。
	* @param i_buffer
	*/
	void NyARMatchPattDeviationColorData::setRaster(const INyARRaster& i_raster)
	{
		//画素フォーマット、サイズ制限
		NyAR_ASSERT(i_raster.isEqualBufferType(NyARBufferType::INT1D_X8R8G8B8_32));
		NyAR_ASSERT(i_raster.getSize().isEqualSize(i_raster.getSize()));

		const int* buf=(const int*)i_raster.getBuffer();
		//i_buffer[XRGB]→差分[R,G,B]変換			
		int i;
		int ave;//<PV/>
		int rgb;//<PV/>
		int* linput=this->_data;//<PV/>

		// input配列のサイズとwhも更新// input=new int[height][width][3];
		int number_of_pixels=this->_number_of_pixels;
		int for_mod=this->_optimize_for_mod;

		//<平均値計算(FORの1/8展開)>
		ave = 0;
		for(i=number_of_pixels-1;i>=for_mod;i--){
			rgb = buf[i];ave += ((rgb >> 16) & 0xff) + ((rgb >> 8) & 0xff) + (rgb & 0xff);
		}
		for (;i>=0;) {
			rgb = buf[i];ave += ((rgb >> 16) & 0xff) + ((rgb >> 8) & 0xff) + (rgb & 0xff);i--;
			rgb = buf[i];ave += ((rgb >> 16) & 0xff) + ((rgb >> 8) & 0xff) + (rgb & 0xff);i--;
			rgb = buf[i];ave += ((rgb >> 16) & 0xff) + ((rgb >> 8) & 0xff) + (rgb & 0xff);i--;
			rgb = buf[i];ave += ((rgb >> 16) & 0xff) + ((rgb >> 8) & 0xff) + (rgb & 0xff);i--;
			rgb = buf[i];ave += ((rgb >> 16) & 0xff) + ((rgb >> 8) & 0xff) + (rgb & 0xff);i--;
			rgb = buf[i];ave += ((rgb >> 16) & 0xff) + ((rgb >> 8) & 0xff) + (rgb & 0xff);i--;
			rgb = buf[i];ave += ((rgb >> 16) & 0xff) + ((rgb >> 8) & 0xff) + (rgb & 0xff);i--;
			rgb = buf[i];ave += ((rgb >> 16) & 0xff) + ((rgb >> 8) & 0xff) + (rgb & 0xff);i--;
		}
		//<平均値計算(FORの1/8展開)/>
		ave=number_of_pixels*255*3-ave;
		ave =255-(ave/ (number_of_pixels * 3));//(255-R)-ave を分解するための事前計算

		int sum = 0,w_sum;
		int input_ptr=number_of_pixels*3-1;
		//<差分値計算(FORの1/8展開)>
		for (i = number_of_pixels-1; i >= for_mod;i--) {
			rgb = buf[i];
			w_sum = (ave - (rgb & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//B
			w_sum = (ave - ((rgb >> 8) & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//G
			w_sum = (ave - ((rgb >> 16) & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//R
		}
		for (; i >=0;) {
			rgb = buf[i];i--;
			w_sum = (ave - (rgb & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//B
			w_sum = (ave - ((rgb >> 8) & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//G
			w_sum = (ave - ((rgb >> 16) & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//R
			rgb = buf[i];i--;
			w_sum = (ave - (rgb & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//B
			w_sum = (ave - ((rgb >> 8) & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//G
			w_sum = (ave - ((rgb >> 16) & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//R
			rgb = buf[i];i--;
			w_sum = (ave - (rgb & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//B
			w_sum = (ave - ((rgb >> 8) & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//G
			w_sum = (ave - ((rgb >> 16) & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//R
			rgb = buf[i];i--;
			w_sum = (ave - (rgb & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//B
			w_sum = (ave - ((rgb >> 8) & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//G
			w_sum = (ave - ((rgb >> 16) & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//R
			rgb = buf[i];i--;
			w_sum = (ave - (rgb & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//B
			w_sum = (ave - ((rgb >> 8) & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//G
			w_sum = (ave - ((rgb >> 16) & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//R
			rgb = buf[i];i--;
			w_sum = (ave - (rgb & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//B
			w_sum = (ave - ((rgb >> 8) & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//G
			w_sum = (ave - ((rgb >> 16) & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//R
			rgb = buf[i];i--;
			w_sum = (ave - (rgb & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//B
			w_sum = (ave - ((rgb >> 8) & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//G
			w_sum = (ave - ((rgb >> 16) & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//R
			rgb = buf[i];i--;
			w_sum = (ave - (rgb & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//B
			w_sum = (ave - ((rgb >> 8) & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//G
			w_sum = (ave - ((rgb >> 16) & 0xff)) ;linput[input_ptr--] = w_sum;sum += w_sum * w_sum;//R
		}
		//<差分値計算(FORの1/8展開)/>
		const double p=sqrt((double) sum);
		this->_pow=p!=0.0?p:0.0000001;
		return;
	}



}
