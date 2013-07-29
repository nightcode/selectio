/*
 * Copyright (C) 2012 The NightCode Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.nightcode.gwt.selectio.server.service;

import org.nightcode.gwt.selectio.server.domain.Item;

import java.util.ArrayList;
import java.util.List;

class SelectioServiceImpl implements SelectioService {

  private static final String STOCK_IDS
      = "ЮЮЮЮ|A|AA|AAPL|ABC|ABT|ACS|ADBE|ADI|ADM|ADP|ADSK|AEE|AEP|AES|AET|AFL|AGN|"
      + "AIG|AIV|AIZ|AKAM|AKS|ALL|ALTR|AMAT|AMD|AMGN|AMP|AMT|AMZN|AN|ANF|AON|"
      + "APA|APC|APD|APOL|ASH|ATI|AVB|AVP|AVY|AXP|AYE|AZO|BA|BAC|BAX|BBBY|"
      + "BBT|BBY|BCR|BDK|BDX|BEN|BF.B|BHI|BIG|BIIB|BJS|BK|BLL|BMC|BMS|BMY|"
      + "BRCM|BRK.B|BSX|BTU|BXP|C|CA|CAG|CAH|CAM|CAT|CB|CBE|CBG|CBS|CCE|CCL|"
      + "CEG|CELG|CEPH|CF|CFN|CHK|CHRW|CI|CINF|CL|CLF|CLX|CMA|CMCSA|CME|CMI|"
      + "CMS|CNP|CNX|COF|COG|COH|COL|COP|COST|CPB|CPWR|CRM|CSC|CSCO|CSX|CTAS|"
      + "CTL|CTSH|CTXS|CVH|CVS|CVX|D|DD|DE|DELL|DF|DFS|DGX|DHI|DHR|DIS|DNB|"
      + "DNR|DO|DOV|DOW|DPS|DRI|DTE|DTV|DUK|DV|DVA|DVN|EBAY|ECL|ED|EFX|EIX|"
      + "EK|EL|EMC|EMN|EMR|EOG|EP|EQR|EQT|ERTS|ESRX|ETFC|ETN|ETR|EXC|EXPD|"
      + "EXPE|F|FAST|FCX|FDO|FDX|FE|FHN|FII|FIS|FISV|FITB|FLIR|FLR|FLS|FMC|"
      + "FO|FPL|FRX|FSLR|FTI|FTR|GAS|GCI|GD|GE|GENZ|GILD|GIS|GLW|GME|GNW|"
      + "GOOG|GPC|GPS|GR|GS|GT|GWW|HAL|HAR|HAS|HBAN|HCBK|HCN|HCP|HD|HES|HIG|"
      + "HNZ|HOG|HON|HOT|HPQ|HRB|HRL|HRS|HSP|HST|HSY|HUM|IBM|ICE|IFF|IGT|"
      + "INTC|INTU|IP|IPG|IRM|ISRG|ITT|ITW|IVZ|JBL|JCI|JCP|JDSU|JEC|JNJ|JNPR|"
      + "JNS|JPM|JWN|K|KEY|KFT|KG|KIM|KLAC|KMB|KO|KR|KSS|L|LEG|LEN|LH|LIFE|"
      + "LLL|LLTC|LLY|LM|LMT|LNC|LO|LOW|LSI|LTD|LUK|LUV|LXK|M|MA|MAR|MAS|MAT|"
      + "MCD|MCHP|MCK|MCO|MDP|MDT|MEE|MET|MFE|MHP|MHS|MI|MIL|MJN|MKC|MMC|MMM|"
      + "MO|MOLX|MON|MOT|MRK|MRO|MS|MSFT|MTB|MU|MUR|MWV|MWW|MXB|MYL|NBL|NBR|"
      + "NDAQ|NEM|NI|NKE|NOC|NOV|NOVL|NSC|NSM|NTAP|NTRS|NU|NUE|NVDA|NVLS|NWL|"
      + "NWSA|NYT|NYX|ODP|OI|OMC|ORCL|ORLY|OXY|PAYX|PBCT|PBG|PBI|PCAR|PCG|"
      + "PCL|PCP|PCS|PDCO|PEG|PEP|PFE|PFG|PG|PGN|PGR|PH|PHM|PKI|PLD|PLL|PM|"
      + "PNC|PNW|POM|PPG|PPL|PRU|PSA|PTV|PWR|PX|PXD|Q|QCOM|QLGC|R|RAI|RDC|RF|"
      + "RHI|RHT|RL|ROK|ROP|ROST|RRC|RRD|RSG|RSH|RTN|RX|S|SAI|SBUX|SCG|SCHW|"
      + "SE|SEE|SHLD|SHW|SIAL|SII|SJM|SLB|SLE|SLM|SNA|SNDK|SNI|SO|SPG|SPLS|"
      + "SRCL|SRE|STI|STJ|STR|STT|STZ|SUN|SVU|SWK|SWN|SWY|SYK|SYMC|SYY|T|TAP|"
      + "TDC|TE|TEG|TER|TGT|THC|TIE|TIF|TJX|TLAB|TMK|TMO|TROW|TRV|TSN|TSO|"
      + "TSS|TWC|TWX|TXN|TXT|UNH|UNM|UNP|UPS|USB|UTX|V|VAR|VFC|VIAb|VLO|VMC|"
      + "VNO|VRSN|VTR|VZ|WAG|WAT|WDC|WEC|WFC|WFMI|WFR|WHR|WIN|WLP|WM|WMB|WMT|"
      + "WPI|WPO|WU|WY|WYN|WYNN|X|XEL|XL|XLNX|XOM|XRAY|XRX|XTO|YHOO|YUM|ZIONZMH";

  private final static String[] DATA;

  static {
    DATA = STOCK_IDS.split("\\|");
  }

  @Override public List<Item> getItems(String query, int start, int length) {
    List<String> matched = new ArrayList<String>();
    for (String st : DATA) {
      if (st.toUpperCase().contains(query.toUpperCase())) {
        matched.add(st);
      }
    }

    int end = Math.min(matched.size(), start + length);
    end = Math.min(matched.size(), end);
    List<String> subMatched = matched.subList(start, end);
    int i = 0;
    List<Item> items = new ArrayList<Item>();
    for (String it : subMatched) {
      Item item = new Item();
      item.setId(it + i++);
      item.setDisplayName(it);
      item.setDescription("descr:" + it);
      items.add(item);
    }
    return items;
  }
}
