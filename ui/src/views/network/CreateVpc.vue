// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
<template>
  <div class="form-layout" v-ctrl-enter="handleSubmit">
    <a-spin :spinning="loading">
      <a-form
        :ref="formRef"
        :model="form"
        :rules="rules"
        layout="vertical"
       >
        <a-form-item name="name" ref="name">
          <template #label>
            <tooltip-label :title="$t('label.name')" :tooltip="apiParams.name.description"/>
          </template>
          <a-input
            v-model:value="form.name"
            :placeholder="apiParams.name.description"
            v-focus="true"/>
        </a-form-item>
        <a-form-item name="displaytext" ref="displaytext">
          <template #label>
            <tooltip-label :title="$t('label.displaytext')" :tooltip="apiParams.displaytext.description"/>
          </template>
          <a-input
            v-model:value="form.displaytext"
            :placeholder="apiParams.displaytext.description"/>
        </a-form-item>
        <a-form-item name="zoneid" ref="zoneid">
          <template #label>
            <tooltip-label :title="$t('label.zoneid')" :tooltip="apiParams.zoneid.description"/>
          </template>
          <a-select
            :loading="loadingZone"
            v-model:value="form.zoneid"
            @change="val => changeZone(val)"
            showSearch
            optionFilterProp="label"
            :filterOption="(input, option) => {
              return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0
            }" >
            <a-select-option v-for="zone in zones" :key="zone.id" :label="zone.name">
              <span>
                <resource-icon v-if="zone.icon" :image="zone.icon.base64image" size="1x" style="margin-right: 5px"/>
                <global-outlined v-else style="margin-right: 5px" />
                {{ zone.name }}
              </span>
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item name="cidr" ref="cidr">
          <template #label>
            <tooltip-label :title="$t('label.cidr')" :tooltip="apiParams.cidr.description"/>
          </template>
          <a-input
            v-model:value="form.cidr"
            :placeholder="apiParams.cidr.description"/>
        </a-form-item>
        <a-form-item name="networkdomain" ref="networkdomain">
          <template #label>
            <tooltip-label :title="$t('label.networkdomain')" :tooltip="apiParams.networkdomain.description"/>
          </template>
          <a-input
            v-model:value="form.networkdomain"
            :placeholder="apiParams.networkdomain.description"/>
        </a-form-item>
        <a-form-item name="vpcofferingid" ref="vpcofferingid">
          <template #label>
            <tooltip-label :title="$t('label.vpcofferingid')" :tooltip="apiParams.vpcofferingid.description"/>
          </template>
          <a-select
            :loading="loadingOffering"
            v-model:value="form.vpcofferingid"
            showSearch
            optionFilterProp="label"
            :filterOption="(input, option) => {
              return option.children[0].children.toLowerCase().indexOf(input.toLowerCase()) >= 0
            }" >
            <a-select-option :value="offering.id" v-for="offering in vpcOfferings" :key="offering.id">
              {{ offering.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item name="start" ref="start">
          <template #label>
            <tooltip-label :title="$t('label.start')" :tooltip="apiParams.start.description"/>
          </template>
          <a-switch v-model:checked="form.start" />
        </a-form-item>
      </a-form>
      <div :span="24" class="action-button">
        <a-button @click="closeAction">{{ $t('label.cancel') }}</a-button>
        <a-button :loading="loading" ref="submit" type="primary" @click="handleSubmit">{{ $t('label.ok') }}</a-button>
      </div>
    </a-spin>
  </div>
</template>
<script>
import { ref, reactive, toRaw } from 'vue'
import { api } from '@/api'
import ResourceIcon from '@/components/view/ResourceIcon'
import TooltipLabel from '@/components/widgets/TooltipLabel'

export default {
  name: 'CreateVpc',
  components: {
    ResourceIcon,
    TooltipLabel
  },
  data () {
    return {
      loading: false,
      loadingZone: false,
      loadingOffering: false,
      zones: [],
      vpcOfferings: []
    }
  },
  beforeCreate () {
    this.apiParams = this.$getApiParams('createVPC')
  },
  created () {
    this.initForm()
    this.fetchData()
  },
  methods: {
    initForm () {
      this.formRef = ref()
      this.form = reactive({
        start: true
      })
      this.rules = reactive({
        name: [{ required: true, message: this.$t('message.error.required.input') }],
        displaytext: [{ required: true, message: this.$t('message.error.required.input') }],
        zoneid: [{ required: true, message: this.$t('label.required') }],
        cidr: [{ required: true, message: this.$t('message.error.required.input') }],
        vpcofferingid: [{ required: true, message: this.$t('label.required') }]
      })
    },
    fetchData () {
      this.fetchZones()
    },
    fetchZones () {
      this.loadingZone = true
      api('listZones', { showicon: true }).then((response) => {
        const listZones = response.listzonesresponse.zone || []
        this.zones = listZones.filter(zone => !zone.securitygroupsenabled)
        this.form.zoneid = ''
        if (this.zones.length > 0) {
          this.form.zoneid = this.zones[0].id
          this.changeZone(this.form.zoneid)
        }
      }).finally(() => {
        this.loadingZone = false
      })
    },
    changeZone (value) {
      this.form.zoneid = value
      if (this.form.zoneid === '') {
        this.form.vpcofferingid = ''
        return
      }
      this.fetchOfferings()
    },
    fetchOfferings () {
      this.loadingOffering = true
      api('listVPCOfferings', { zoneid: this.form.zoneid, state: 'Enabled' }).then((reponse) => {
        this.vpcOfferings = reponse.listvpcofferingsresponse.vpcoffering
        this.form.vpcofferingid = this.vpcOfferings[0].id || ''
      }).finally(() => {
        this.loadingOffering = false
      })
    },
    closeAction () {
      this.$emit('close-action')
    },
    handleSubmit (e) {
      e.preventDefault()
      if (this.loading) return
      this.formRef.value.validate().then(() => {
        const values = toRaw(this.form)
        const params = {}
        for (const key in values) {
          const input = values[key]
          if (input === '' || input === null || input === undefined) {
            continue
          }
          params[key] = input
        }
        this.loading = true
        const title = this.$t('label.add.vpc')
        const description = this.$t('message.success.add.vpc.network')
        api('createVPC', params).then(json => {
          const jobId = json.createvpcresponse.jobid
          if (jobId) {
            this.$pollJob({
              jobId,
              title,
              description,
              loadingMessage: `${title} ${this.$t('label.in.progress')}`,
              catchMessage: this.$t('error.fetching.async.job.result')
            })
          }
          this.closeAction()
        }).catch(error => {
          this.$notifyError(error)
        }).finally(() => {
          this.loading = false
        })
      }).catch(error => {
        this.formRef.value.scrollToField(error.errorFields[0].name)
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.form-layout {
  width: 80vw;
  @media (min-width: 700px) {
    width: 600px;
  }
}

.form {
  margin: 10px 0;
}
</style>
