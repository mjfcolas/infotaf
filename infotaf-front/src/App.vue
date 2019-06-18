<template>
  <div id="app">
    <div class="container">
      <div class="row">
        <div class="col-md-6" id="site-title">
          <header-title></header-title>
        </div>
        <div class="col-md-6">
          <span class="pull-right">
            <global-infos></global-infos>
            <login></login>
            <account></account>
            <administration></administration>
          </span>
        </div>
      </div>
      <div class="row">
        <div class="col-md-8">
          <display-pg></display-pg>
        </div>
        <div class="col-md-4">
          <news></news>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import News from '@/components/news/News'
import Login from '@/components/login/Login'
import Account from '@/components/account/Account'
import GlobalInfos from '@/components/globalInfos/GlobalInfos'
import HeaderTitle from '@/components/headerTitle/HeaderTitle'
import Administration from '@/components/administration/Administration'
import DisplayPg from '@/components/displayPg/DisplayPg'

export default {
  name: 'app',
  components: { News, Login, Account, GlobalInfos, HeaderTitle, Administration, DisplayPg }
}
</script>

<style>
  @import url(https://fonts.googleapis.com/css?family=Roboto:400,500,700,300,100);

  body {
    font-family: "Roboto", helvetica, arial, sans-serif;
    font-size: 16px;
    font-weight: 400;
    text-rendering: optimizeLegibility;
  }
  textarea{
    resize:none;
  }
  .ROLE_ADM:not(.granted), .ROLE_USR:not(.granted){
    display:none;
  }
  .small-input{
    width:75px;
  }
  .text-color-red{
    color: red;
  }
  .text-color-black{
    color: black;
  }
  .text-color-green{
    color: green;
  }
  <!--Style des tables-->
  .custom-table-style{
    background: white;
    border-radius:3px;
    border-collapse: collapse;
    margin: auto;
    padding:5px;
    width: 100%;
    box-shadow: 0 5px 10px rgba(0, 0, 0, 0.1);
    animation: float 5s infinite;
  }

  .custom-table-style th {
    color:#D5DDE5;;
    background:#1b1e24;
    border-bottom:4px solid #9ea7af;
    border-right: 1px solid #343a45;
    font-size:23px;
    font-weight: 300;
    padding:3px;
    text-align:left;
    text-shadow: 0 1px 1px rgba(0, 0, 0, 0.1);
    vertical-align:middle;
  }

  .custom-table-style th:first-child {
    border-top-left-radius:3px;
  }

  .custom-table-style th:last-child {
    border-top-right-radius:3px;
    border-right:none;
  }

  .custom-table-style tr {
    border-top: 1px solid #C1C3D1;
    border-bottom-: 1px solid #C1C3D1;
    color:#000000;
    font-size:16px;
    font-weight:normal;
    text-shadow: 0 1px 1px rgba(256, 256, 256, 0.1);
  }

  .custom-table-style tr:hover td {
    background:#4E5066;
    color:#000000;
    border-top: 1px solid #22262e;
    border-bottom: 1px solid #22262e;
  }

  .custom-table-style tr:first-child {
    border-top:none;
  }

  .custom-table-style tr:last-child {
    border-bottom:none;
  }

  .custom-table-style tr:nth-child(odd) td {
    background:#EBEBEB;
  }

  .custom-table-style tr:nth-child(odd):hover td {
    background:#4E5066;
  }

  .custom-table-style tr:last-child td:first-child {
    border-bottom-left-radius:3px;
  }

  .custom-table-style tr:last-child td:last-child {
    border-bottom-right-radius:3px;
  }

  .custom-table-style td {
    background:#FFFFFF;
    padding:2px;
    text-align:left;
    vertical-align:middle;
    font-weight:bold;;
    font-size:13px;
    text-shadow: -1px -1px 1px rgba(0, 0, 0, 0.1);
    border-right: 1px solid #C1C3D1;
  }

  .custom-table-style td:last-child {
    border-right: 0px;
  }

  .custom-table-style th.text-left {
    text-align: left;
  }

  .custom-table-style th.text-center {
    text-align: center;
  }

  .custom-table-style th.text-right {
    text-align: right;
  }

  .custom-table-style td.text-left {
    text-align: left;
  }

  .custom-table-style td.text-center {
    text-align: center;
  }

  .custom-table-style td.text-right {
    text-align: right;
  }
  .color-red {
    color:red;
  }
</style>
