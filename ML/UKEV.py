#!/usr/bin/env python
# coding: utf-8

# In[1]:


import pandas as pd
from datetime import datetime
import seaborn as sns


# In[2]:


df=pd.read_csv('road-transport-fuel-consumption-tables-2005-2021-3.csv')


# In[3]:


df


# In[4]:


df.columns


# In[5]:


df[['local_authority_code', 'region', 'local_authority', 'buses_motorways',
       'buses_a_roads', 'buses_minor_roads', 'buses_total',
       'diesel_cars_motorways', 'diesel_cars_a_roads',
       'diesel_cars_minor_roads', 'diesel_cars_total', 'petrol_cars_motorways',
       'petrol_cars_a_roads', 'petrol_cars_minor_roads', 'petrol_cars_total',
       'motorcycles_motorways', 'motorcycles_a_roads',
       'motorcycles_minor_roads', 'motorcycles_total', 'hgv_motorways',
       'hgv_a_roads', 'hgv_minor_roads', 'hgv_total', 'diesel_lgv_motorways',
       'diesel_lgv_a_roads', 'diesel_lgv_minor_roads', 'diesel_lgv_total',
       'petrol_lgv_motorways', 'petrol_lgv_a_roads', 'petrol_lgv_minor_roads',
       'petrol_lgv_total', 'personal_transport', 'freight_transport',
       'total_all_vehicle', 'total_all_vehicle_bioenergy']]


# In[6]:


df=df[['region', 'local_authority','diesel_cars_total','petrol_cars_total','total_all_vehicle','total_all_vehicle_bioenergy']]


# In[7]:


df


# In[8]:


df.dtypes


# In[9]:


df['Year'] = '31-12-2005'
df['Year']=pd.to_datetime(df['Year'])


# In[10]:


df.describe()


# In[11]:


df


# In[12]:


df.isna().sum()


# In[13]:


df_new=df.dropna()


# In[14]:


df_new


# In[15]:


df_new.columns


# In[16]:


df_new.replace(',','', regex=True, inplace=True)


# In[17]:


df_new.tail()


# In[18]:


df_new.loc[df_new.duplicated()]


# In[19]:


# Check for duplicate coaster name
df_new.loc[df_new.duplicated(subset=['region', 'local_authority', 'diesel_cars_total', 'petrol_cars_total',
       'total_all_vehicle', 'total_all_vehicle_bioenergy', 'Year'])]


# In[20]:


import matplotlib.pyplot as plt


# In[21]:


df_new.value_counts()


# In[22]:


df_new['diesel_cars_total']=pd.to_numeric(df_new['diesel_cars_total'], errors='coerce')


# In[23]:


df_new['petrol_cars_total']=pd.to_numeric(df_new['petrol_cars_total'], errors='coerce')


# In[24]:


df_new.dtypes


# In[25]:


df_new['total_all_vehicle']=pd.to_numeric(df_new['total_all_vehicle'], errors='coerce')


# In[26]:


df_new['total_all_vehicle_bioenergy']=pd.to_numeric(df_new['total_all_vehicle_bioenergy'], errors='coerce')


# In[27]:


df_new.dtypes


# In[28]:


ax = df_new['diesel_cars_total'].value_counts()     .head(10)     .plot(kind='bar')


# In[29]:


df_new['diesel_cars_total'].plot(kind='hist')


# In[30]:


ax = df_new['diesel_cars_total'].plot(kind='kde')
                   


# In[31]:


df_new.head(10).plot(kind='scatter',
        x='diesel_cars_total',
        y='local_authority',
        title='Diesel cars in different counties')
plt.show()


# In[140]:


pip install darts


# In[32]:


from darts.dataprocessing.transformers import Scaler


# In[48]:


df_diesel=df_new[['diesel_cars_total','Year']]


# In[53]:


df_diesel


# In[55]:


df_diesel=df_diesel.set_index('Year')


# In[68]:


df_diesel


# In[69]:


import xgboost as xgb
from sklearn.metrics import mean_squared_error
color_pal = sns.color_palette()
plt.style.use('fivethirtyeight')


# In[70]:


df_diesel.plot(style='.',
        figsize=(15, 5),
        color=color_pal[0],
        title='Diesel use')
plt.show()


# In[71]:


df_diesel.index = pd.to_datetime(df_diesel.index)


# In[72]:


df_diesel.dtypes


# In[73]:


df2=pd.read_csv('road-transport-fuel-consumption-tables-2005-2021-4.csv')


# In[75]:


df2=df2.dropna()


# In[76]:


df2=df2[['region', 'local_authority','diesel_cars_total','petrol_cars_total','total_all_vehicle','total_all_vehicle_bioenergy']]


# In[78]:


df2['Year'] = '31-12-2006'
df2['Year']=pd.to_datetime(df2['Year'])


# In[79]:


df2


# In[81]:


df2.replace(',','', regex=True, inplace=True)


# In[82]:


df2


# In[83]:


df2['diesel_cars_total']=pd.to_numeric(df2['diesel_cars_total'], errors='coerce')
df2['total_all_vehicle']=pd.to_numeric(df2['total_all_vehicle'], errors='coerce')
df2['petrol_cars_total']=pd.to_numeric(df2['petrol_cars_total'], errors='coerce')


# In[84]:


df2.dtypes


# In[85]:


df_56=pd.concat([df_new,df2])


# In[86]:


df_56


# In[88]:


df_56=df_56.set_index('Year')


# In[89]:


df_56


# In[92]:


df_56.plot(style='.',
        figsize=(15, 5),
        color=color_pal[0],
        title='Diesel use')
plt.show()


# In[94]:


df_56.index


# In[101]:


train = df_56.loc[df_56.index<='2005-12-31']

#train = df_56.loc[df_56.index='2005-12-31']
test = df_56.loc[df_56.index>='2006-12-31']

fig, ax = plt.subplots(figsize=(15, 5))
train.plot(ax=ax, label='Training Set', title='Data Train/Test Split')
test.plot(ax=ax, label='Test Set')
ax.axvline('01-01-2015', color='black', ls='--')
ax.legend(['Training Set', 'Test Set'])
plt.show()


# In[100]:


df_56.loc[(df_56.index <= '2005-12-31') & (df_56.index >= '2006-12-31')]     .plot(figsize=(15, 5), title='Week Of Data')
plt.show()


# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[41]:


import numpy as np


# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[43]:


np_diesel=df_diesel.to_numpy()


# In[44]:


np_diesel


# In[46]:


np_diesel=Scaler()


# In[47]:


series_scaled=np_diesel.fit_transform(np_diesel)


# In[ ]:





# In[ ]:





# In[35]:


df_diesel=Scaler()


# In[ ]:





# In[37]:


series_scaled=df_diesel.fit_transform(df_diesel)


# In[ ]:





# In[ ]:





# In[9]:


df=df[['diesel_cars_total','petrol_cars_total','total_all_vehicle','Year']]


# In[10]:


df.isna().sum()
df_new=df.dropna()


# In[11]:


df.replace(',','', regex=True, inplace=True)
df.dropna(axis=0)


# In[12]:


df_new.tail()


# In[ ]:





# In[13]:



df['diesel_cars_total'] = df['diesel_cars_total'].astype(str)


# In[16]:




displot = sns.displot(data = df, x = 'diesel_cars_total')

displot.set_xticklabels(rotation=90)

plt.show()


# In[ ]:


df.info()


# In[ ]:


import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

plt.figure(figsize=(10, 6))
plt.plot(df['diesel_cars_total'].astype(str), label='Diesel Cars')


plt.xlabel('Year - 2005')
plt.title('')
plt.legend()


plt.show()


# In[ ]:


dfs=pd.read_csv('road-transport-fuel-consumption-tables-2005-2021-4.csv')


# In[ ]:


dfs.dropna()


# In[ ]:


dfs['Year'] = '31-12-2006'
dfs['Year']=pd.to_datetime(dfs['Year'])


# In[ ]:


dfs


# In[ ]:


dfs=dfs[['total_all_vehicle','Year']]


# In[ ]:


df2 = pd.concat([df, dfs], ignore_index=True, sort=False)


# In[ ]:


df2


# In[ ]:


df2.plot(label='Total Vehicle')
plt.legend()


# In[ ]:


df5=pd.read_csv('road-transport-fuel-consumption-tables-2005-2021-5.csv')


# In[ ]:


df5.dropna()


# In[ ]:


df5['Year'] = '31-12-2007'
df5['Year']=pd.to_datetime(df5['Year'])


# In[ ]:


df5=df5[['total_all_vehicle','Year']]


# In[ ]:


df_3 = pd.concat([df2, df5], ignore_index=True, sort=False)


# In[ ]:


df_3


# In[ ]:


df_3.plot(label='Total Vehicle')
plt.legend()


# In[ ]:




